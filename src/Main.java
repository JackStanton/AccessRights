
import Classes.DBWorker;
import Classes.EncryptionUntil;
import GUI.MainWindow;
import org.sqlite.core.DB;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, IOException, ClassNotFoundException, SQLException {
        DBWorker.conn();
        new MainWindow();
//        EncryptionUntil.generateKey();
//        DBWorker.resetRights();
    }

}

