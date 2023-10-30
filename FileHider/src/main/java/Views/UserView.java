package Views;

import DAO.DataDAO;
import Model.Data;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserView {
    private String email;

    public UserView(String email) {
        this.email = email;
    }
    public void home() {

        do {
            System.out.println("Welcome " + this.email);
            System.out.println("Press 1 to show hidden files");
            System.out.println("Press 2 to hide a new file");
            System.out.println("Press 3 to unhide a file");
            System.out.println("Press 0 to Exit");
            Scanner in = new Scanner(System.in);
            System.out.print("Enter the option: ");
            int option = Integer.parseInt(in.nextLine());

            switch (option) {
                case 1 -> {
                    try {
                        List<Data> files = DataDAO.getAllFiles(this.email);
                        System.out.println("ID - File Name");
                        for (Data file : files) {
                            System.out.println(file.getId() + " - " + file.getFileName());
                        }
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    }
                }
                case 2 -> {
                    System.out.print("Enter the file path: ");
                    String path = in.nextLine();
                    File f = new File(path);
                    Data file = new Data(f.getName(), path, email);
                    try {
                        DataDAO.hideFile(file);
                    } catch (SQLException e) {
                        System.out.println(e.getMessage());
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 3 -> {
                    try {
                        List<Data> files = DataDAO.getAllFiles(this.email);
                        System.out.println("ID - File Name");
                        for (Data file : files) {
                            System.out.println(file.getId() + " - " + file.getFileName());
                        }
                        System.out.print("Enter the id: ");
                        int id = Integer.parseInt(in.nextLine());
                        boolean isValid = false;
                        for (Data file : files) {
                            if (file.getId() == id) {
                                isValid = true;
                                break;
                            }
                        }
                        if(isValid) {
                            DataDAO.unhide(id);
                        } else {
                            System.out.println("Invalid Id");
                        }
                    } catch (SQLException | IOException e) {
                        System.out.println(e.getMessage());
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                }

                case 0 -> System.exit(0);
            }
        } while (true);
    }
}
