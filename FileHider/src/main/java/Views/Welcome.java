package Views;

import DAO.UserDAO;
import Model.User;
import Services.GenerateOTP;
import Services.SendOTPService;
import Services.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

public class Welcome {
    public void welcomeScreen() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the app");
        System.out.println("Press 1 to login");
        System.out.println("Press 2 to signup");
        System.out.println("Press 0 to exit");
        int choice = 0;

        try {
            System.out.print("Please enter your option: ");
            choice = Integer.parseInt(br.readLine());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        if (choice == 1) {
            login();
        } else if (choice == 2) {
            signUp();
        } else if (choice == 0) {
            System.exit(0);
        } else {
            System.out.println("Invalid Entry!!");
        }
    }
    static int count = 0;
    private void login() {
        Scanner in = new Scanner(System.in);
        System.out.print("Enter E-mail: ");
        String email = in.nextLine();
        try {
            while (true) {
                if (UserDAO.isExist(email)) {
                    String genOTP = GenerateOTP.getOTP();
                    SendOTPService.sendOTP(email, genOTP);
                    System.out.print("Enter the OTP: ");
                    String otp = in.nextLine();
                    if (otp.equals(genOTP)) {
                        System.out.println("OTP verified!");
                        new UserView(email).home();
                    } else {
                        count++;
                        System.out.println(4 - count + " tries left");
                        System.out.println("Resending the OTP");
                        if (count == 4) {
                            System.out.println("login failed!!");
                            System.exit(0);
                        }
                    }
                } else {
                    System.out.println("User not found");
                    System.out.println("Press 1 for SignUp");
                    System.out.println("Press 2 for trying again");
                    System.out.println("press 0 to exit");
                    int response = in.nextInt();
                    if (response == 1) {
                        signUp();
                    } else if (response == 2) {
                        continue;
                    } else if (response == 3) {
                        System.exit(0);
                    } else {
                        System.out.println("Invalid Entry!!");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void signUp() {
        count = 0;
        Scanner in = new Scanner(System.in);
        System.out.print("Enter name: ");
        String name = in.nextLine();
        System.out.print("Enter E-mail: ");
        String email = in.nextLine();

        while (true) {
            String genOTP = GenerateOTP.getOTP();
            SendOTPService.sendOTP(email, genOTP);
            System.out.print("Enter the OTP: ");
            String otp = in.nextLine();
            if (otp.equals(genOTP)) {
                User user = new User(name, email);
                int reponse = UserService.saveUser(user);
                switch (reponse) {
                    case 0 -> {
                        System.out.println("Already registered");
                        System.out.println("Try logging in");
                    }
                    case 1 -> {
                        System.out.println("User registered");
                        new UserView(email).home();
                    }
                }
                break;
            } else {
                count++;
                System.out.println(6 - count + " tries left");
                System.out.println("Resending the OTP");
                if (count == 6) {
                    System.out.println("login failed!!");
                    System.exit(0);
                }
            }
        }
    }
}
