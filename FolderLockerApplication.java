import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

public class FolderLockerApplication {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the path of the folder you want to lock: ");
        String folderPath = scanner.nextLine();

        System.out.print("Enter the password to lock the folder: ");
        String password = scanner.nextLine();

        lockFolder(folderPath, password);

        scanner.close();
    }

    private static void lockFolder(String folderPath, String password) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Invalid folder path!");
            return;
        }

        try {
            // Rename the folder
            File lockedFolder = new File(folderPath + "_locked");
            if (lockedFolder.exists()) {
                System.out.println("The folder is already locked!");
                return;
            }

            if (folder.renameTo(lockedFolder)) {
                System.out.println("Folder locked successfully!");

                // Encrypt the folder
                encryptFolder(lockedFolder, password);
            } else {
                System.out.println("Failed to lock the folder!");
            }
        } catch (Exception e) {
            System.out.println("An error occurred while locking the folder: " + e.getMessage());
        }
    }

    private static void encryptFolder(File folder, String password) throws Exception {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    encryptFolder(file, password);
                } else {
                    encryptFile(file, password);
                }
            }
        }
    }

    private static void encryptFile(File file, String password) throws Exception {
        Path sourcePath = file.toPath();
        String encryptedFileName = file.getName() + ".locked";
        Path targetPath = file.toPath().resolveSibling(encryptedFileName);

        Files.move(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
    }
}