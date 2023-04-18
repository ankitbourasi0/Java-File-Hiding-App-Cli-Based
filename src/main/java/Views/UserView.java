package Views;

import DAO.DataDAO;
import Model.Data;
import Model.User;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class UserView {
    public String name;
    public String email;

    UserView(String name, String email) {
        this.name = name;
        this.email = email;
    }

    UserView(String email) {
        this.email = email;
    }

    public void home() {
        do {
            System.out.println("Welcome " + this.email);
            System.out.println("Press 1 to show hidden files");
            System.out.println("Press 2 to hide files");
            System.out.println("Press 3 to unhide files");
            System.out.println("Press 0 to exit");
            Scanner sc = new Scanner(System.in);
            int ch = Integer.parseInt(sc.nextLine());
            switch (ch) {
                case 1: {
                    try {
                        List<Data> files = DataDAO.getAllFiles(this.email);
                        System.out.println("ID - File Name ");
                        for (Data file : files) {
                            System.out.println(file.getId() + " - " + file.getFileName());
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 2: {
                    System.out.println("Copy and paste the file path here !!!");
                    String path = sc.nextLine();
//                    String newpath = path.replace('"', ' ');
//                    String finalpath = newpath.replace(" ","");
                    System.out.println(path);
                    File f = new File(path);
                    Data file = new Data(0, f.getName(), path, this.email);
                    try {
                        DataDAO.hideFile(file);
                    } catch (SQLException | IOException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 3: {

                    try {
                        List<Data> files = DataDAO.getAllFiles(this.email);
                        System.out.println("ID - File Name ");
                        for (Data file : files) {
                            System.out.println(file.getId() + " - " + file.getFileName());
                        }
                        System.out.println("Enter the id of file to unhide ");
                        int id = Integer.parseInt(sc.nextLine());
                        boolean isValidId = false;
                        for (Data file : files) {
                            if (file.getId() == id) {
                                isValidId = true;
                                break;
                            }
                        }
                        if (isValidId) {
                            DataDAO.unhideFile(id);
                        } else {
                            System.out.println("Wrong ID ");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                }
                case 0: {
                    System.exit(0);
                }
            }
        } while (true);
    }
}
