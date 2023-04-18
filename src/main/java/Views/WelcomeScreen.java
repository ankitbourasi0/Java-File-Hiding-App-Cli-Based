package Views;

import DAO.UserDAO;
import Model.User;
import Service.GenerateOTP;
import Service.SendOTPService;
import Service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.Scanner;

public class WelcomeScreen {
    public void Welcome(){
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Welcome to the app");
        System.out.println("Press 1 to login");
        System.out.println("Press 2 to signup");
        System.out.println("Press 0 to exit");
        int tempInput = 0;
        try{
            tempInput = Integer.parseInt(br.readLine());
        }catch (IOException e){
            e.printStackTrace();
        }

        switch (tempInput){
            case 1 : {login();
                    break;}
            case 2 :{ signUp();
                        break;}
            case 3 : System.exit(0);
        }

    }

    private void login() {
        System.out.println("Enter your email");
        Scanner sc = new Scanner(System.in);
        var email = sc.nextLine();
        try{
            if(UserDAO.isExists(email)){
                var genOTP = GenerateOTP.getOTP();
                SendOTPService.sendOTP(email,genOTP);
                System.out.println("Enter the OTP ");
                String userOtp = sc.nextLine();
                 if (userOtp.equals(genOTP)){
                     System.out.println("Welcome");
//                     User u =  new User();
//                     new UserView(u.getName(),u.getEmail()).home();
                        new UserView(email).home();
                 }else{
                     System.out.println("Wrong OTP");
                 }
            }else{
                System.out.println("User not found !!!");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private void signUp() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Enter name");
        String name =sc.nextLine();
        System.out.println("Enter email");
        String email =sc.nextLine();
        String genOTP = GenerateOTP.getOTP();
        SendOTPService.sendOTP(email,genOTP);
        System.out.println("Enter OTP");
        String userOtp = sc.nextLine();
        if ( userOtp.equals(genOTP)){
            User user = new User(name,email);
           int reponse =  UserService.saveUser(user);
           switch (reponse){
               case 0 : System.out.println("User registered");
               case 1 :
                   System.out.println("User already exists");
           }
        }else{
            System.out.println("Wrong OTP");
        }
    }
}
