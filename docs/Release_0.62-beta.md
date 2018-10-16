# [Zcash](https://z.cash/) Desktop GUI Wallet - binary release v0.62-beta

This document describes how to install binary release v0.62-beta of the [Zcash](https://z.cash/) 
Desktop GUI Wallet. This release of the Zcash Desktop GUI Wallet is tested with Zcash version 
[v1.0.6](https://github.com/zcash/zcash/releases/tag/v1.0.6). Users who encounter issues 
not listed in the list of known issues and limitations, are welcome to report them in 
the [issues section](https://github.com/vaklinov/zcash-swing-wallet-ui/issues). 

![Screenshot](https://github.com/vaklinov/zcash-swing-wallet-ui/raw/master/docs/ZCashWalletSmall.png "Main Window")

## Installing and running the Wallet GUI

1. Downloading the wallet
 
   Download file [ZCashSwingWalletUI.jar](https://github.com/vaklinov/zcash-swing-wallet-ui/releases/download/0.62-beta/ZCashSwingWalletUI.jar)
   and place it in a folder like `~/Downloads`. Then please make the JAR executable with a command like:
   ```
   user@ubuntu:~/Downloads$ chmod u+x ./ZCashSwingWalletUI.jar
   ```
   
2. Verifying the download

   **This step is very important!** To verify that the file `ZCashSwingWalletUI.jar` is an authentic release, you
   need to compute its SHA256 checksum, like this:
   ```
   user@ubuntu:~/Downloads$ sha256sum ZCashSwingWalletUI.jar 
   d0921274341c774f4a7ea9a796212f2db45bc1f48d0f6e4bf6af89a875c9f8d5  ZCashSwingWalletUI.jar
   ```
   **If the resulting checksum is not `d0921274341c774f4a7ea9a796212f2db45bc1f48d0f6e4bf6af89a875c9f8d5` then**
   **something is wrong and you should discard the downloaded wallet!**

3. Installing the downloaded Zcash GUI wallet

  3.1. If you have built Zcash from source code:

   Assuming you have already built from source code [Zcash](https://z.cash/) in directory `/home/user/zcash/src` (for 
   example - this is the typical build dir. for Zcash v1.0.6) which contains the command line tools `zcash-cli` 
   and `zcashd` you need to take the file `ZCashSwingWalletUI.jar` and copy it 
   to directory `/home/user/zcash/src` (the same dir. that contains `zcash-cli` and `zcashd`). Example copy command:
   ```
   user@ubuntu:~/Downloads$ cp ./ZCashSwingWalletUI.jar /home/user/zcash/src    
   ```
   
  3.2. If you have installed the Zcash [binary packages](https://github.com/zcash/zcash/wiki/Debian-binary-packages)

   The command line tools `zcash-cli` and `zcashd` are placed by the package installer in:
   ```
   /usr/bin/zcash-cli
   /usr/bin/zcashd
   ```
   The Zcash GUI wallet knows how to find them there. You may place the file  `ZCashSwingWalletUI.jar`
   anywhere in your `/home` directory that you find convenient and start it from there.

4. Running the installed Zcash GUI wallet

   Before running the GUI you need to start zcashd (e.g. `zcashd --daemon`). The wallet GUI is a Java program packaged 
   as an executable JAR file. It may be run from command line or started from another GUI tool (e.g. file manager). 
   Assuming you have already installed [Zcash](https://z.cash/) and the GUI Wallet `ZCashSwingWalletUI.jar` in 
   directory `/home/user/zcash/src` one way to run it from command line is:
   ```
   user@ubuntu:~$ java -jar /home/user/zcash/src/ZCashSwingWalletUI.jar
   ```
   If you are using Ubuntu (or similar ;) Linux you may instead just use the file manager and 
   right-click on the `ZCashSwingWalletUI.jar` file and choose the option "Open with OpenJDK 8 Runtime". 
   This will start the Zcash GUI wallet.

### Donations accepted
At the present time this project is non-commercial in nature and developed by volunteers. If you find the GUI
Wallet useful, please consider making a donation for its further development. Your contribution matters! Donations 
are accepted at Zcash address:
```
t1UMGjLDipdfuCdNwxUZTV4FhM34FJXgM8r
```

### License
This program is distributed under an [MIT License](https://github.com/vaklinov/zcash-swing-wallet-ui/raw/master/LICENSE).

### Disclaimer
This program is not officially endorsed by or associated with the Zcash project and the Zcash company.
[Zcash®](https://trademarks.justia.com/871/93/zcash-87193130.html) and the 
[Zcash® logo](https://trademarks.justia.com/868/84/z-86884549.html) are trademarks of the
[Zerocoin Electric Coin Company](https://trademarks.justia.com/owners/zerocoin-electric-coin-company-3232749/).

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.

### Known issues and limitations

1. Limitation: Wallet encryption has been temporarily disabled in Zcash due to stability problems. A corresponding issue 
[#1552](https://github.com/zcash/zcash/issues/1552) has been opened by the Zcash developers. Correspondingly
wallet encryption has been temporarily disabled in the Zcash Desktop GUI Wallet.
1. Issue: the GUI wallet does not work correctly if zcashd is started with a custom data directory, like:
`zcashd -datadir=/home/data/whatever` This will be fixed in later versions.
1. Issue: GUI data tables (transactions/addresses etc.) allow copying of data via double click but also allow editing. 
The latter needs to be disabled. 
1. Limitation: The list of transactions does not show all outgoing ones (specifically outgoing Z address 
transactions). A corresponding issue [#1438](https://github.com/zcash/zcash/issues/1438) has been opened 
for the Zcash developers - soon to be fixed. A fix for the GUI wallet may be expected within 1-2 weeks. 
1. Limitation: The CPU percentage shown to be taken by zcashd is the average for the entire lifetime of the process. 
This is not very useful. This will be improved in future versions.