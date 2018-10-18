package com.vaklinov.zcashui;

import java.awt.Component;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.ProgressMonitorInputStream;
import javax.xml.bind.DatatypeConverter;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Fetches the proving key.  Deliberately hardcoded.
 * @author zab
 */
public class ProvingKeyFetcher {
    
    private static final int PROVING_KEY_SIZE = 725523612;
    private static final String SHA256 = "b685d700c60328498fbde589c8c7c484c722b788b265b72af448a5bf0ee55b50";
    private static final String URL = "https://z.cash/downloads/sprout-groth16.params";
    // TODO: add backups
    
    public void fetchIfMissing(StartupProgressDialog parent) throws IOException {
        try {
            verifyOrFetch(parent);
        } catch (InterruptedIOException iox) {
            JOptionPane.showMessageDialog(parent, "Zcash cannot proceed without a proving key.");
            System.exit(-3);
        }
    }
    
    private void verifyOrFetch(StartupProgressDialog parent) throws IOException {
        File zCashParams = new File(System.getenv("APPDATA") + "/ZcashParams");
        zCashParams = zCashParams.getCanonicalFile();
        
        boolean needsFetch = false;
        if (!zCashParams.exists()) {
            
            needsFetch = true;
            zCashParams.mkdirs();
        }

	 // verifying key is small, always copy it
        File verifyingKeyFile = new File(zCashParams,"sprout-verifying.key");
        FileOutputStream vfos = new FileOutputStream(verifyingKeyFile);
        InputStream vis = ProvingKeyFetcher.class.getClassLoader().getResourceAsStream("sprout-verifying.key");
        copy(vis,vfos);
        vfos.close();
	
        // output key is small, always copy it
        File outputKeyFile = new File(zCashParams,"sapling-output.params");
        FileOutputStream ofos = new FileOutputStream(outputKeyFile);
        InputStream ois = ProvingKeyFetcher.class.getClassLoader().getResourceAsStream("sapling-output.params");
        copy(ois,ofos);
        ofos.close();

        // spending key is also small, always copy it, too
	File spendingKeyFile = new File(zCashParams,"sapling-spend.params");
	FileOutputStream sfos = new FileOutputStream(spendingKeyFile);
	InputStream sis = ProvingKeyFetcher.class.getClassLoader().getResourceAsStream("sapling-spend.params");
	copy(sis,sfos);
	sfos.close();

	
        File provingKeyFile = new File(zCashParams,"sprout-groth16.params");
        provingKeyFile = provingKeyFile.getCanonicalFile();
        if (!provingKeyFile.exists()) {
            needsFetch = true;
        } else if (provingKeyFile.length() != PROVING_KEY_SIZE) {
            needsFetch = true;
        } else {
            parent.setProgressText("Verifying proving key...");
            needsFetch = !checkSHA256(provingKeyFile,parent);
        }
        
        if (!needsFetch) {
            return;
        }
        
        JOptionPane.showMessageDialog(parent, "Zcash needs to download a large file.  This will happen only once.\n  "
                + "Please be patient.  Press OK to continue");
        
        parent.setProgressText("Downloading proving key...");
        provingKeyFile.delete();
        OutputStream os = new BufferedOutputStream(new FileOutputStream(provingKeyFile));
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpGet get = new HttpGet(URL);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(get);
            InputStream is = response.getEntity().getContent();
            ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(parent, "Downloading proving key", is);
            pmis.getProgressMonitor().setMaximum(PROVING_KEY_SIZE);
            pmis.getProgressMonitor().setMillisToPopup(10);
            
            copy(pmis,os);
            os.close();
        } finally {
            try {if (response != null)response.close();} catch (IOException ignore){}
            try {httpClient.close();} catch (IOException ignore){}
        }
        parent.setProgressText("Verifying downloaded proving key...");
        if (!checkSHA256(provingKeyFile, parent)) {
            JOptionPane.showMessageDialog(parent, "Failed to download proving key.  Cannot continue");
            System.exit(-4);
        }
    }
            

    private static void copy(InputStream is, OutputStream os) throws IOException {
        byte[] buf = new byte[0x1 << 13];
        int read;
        while ((read = is.read(buf)) >- 0) {
            os.write(buf,0,read);
        }
        os.flush();
    }
    
    private static boolean checkSHA256(File provingKey, Component parent) throws IOException {
        MessageDigest sha256;
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException impossible) {
            throw new RuntimeException(impossible);
        }
        try (InputStream is = new BufferedInputStream(new FileInputStream(provingKey))) {
            ProgressMonitorInputStream pmis = new ProgressMonitorInputStream(parent,"Verifying proving key",is);
            pmis.getProgressMonitor().setMaximum(PROVING_KEY_SIZE);
            pmis.getProgressMonitor().setMillisToPopup(10);
            DigestInputStream dis = new DigestInputStream(pmis, sha256);
            byte [] temp = new byte[0x1 << 13];
            while(dis.read(temp) >= 0);
            byte [] digest = sha256.digest();
            return SHA256.equalsIgnoreCase(DatatypeConverter.printHexBinary(digest));
        }
    }
}
