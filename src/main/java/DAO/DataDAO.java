package DAO;

import DB.JdbcConnection;
import Model.Data;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataDAO {

    public static List<Data> getAllFiles(String email) throws SQLException {
        Connection connection = JdbcConnection.getConnection();

//        System.out.println(email+ "this is the mail");
        PreparedStatement ps = connection.prepareStatement("select * from data where email = ? ");
        ps.setString(1,email);
        ResultSet rs =  ps.executeQuery();
        List<Data> files = new ArrayList<>();
        while (rs.next()){
            int id = rs.getInt(1);
            String name = rs.getString(2);
            String path = rs.getString(3);
//            System.out.println(id + name+ path);
            files.add(new Data(id,name,path));
        }
        return files;
    }
    public static int hideFile(Data file) throws SQLException, IOException {
        Connection connection = JdbcConnection.getConnection();
        PreparedStatement ps  = connection.prepareStatement("insert into data(data_name,path,email,bin_data) values(?,?,?,?)");
        ps.setString(1,file.getFileName());
        ps.setString(2,file.getPath());
        ps.setString(3,file.getEmail());
        // for blob data accessing path then read
        File f = new File(file.getPath());
        FileReader fr = new FileReader(f);
//        FileInputStream fr = new FileInputStream(f);
        //reading and saving with stream
        ps.setCharacterStream(4,fr,f.length());
//            ps.setBlob(4,fr);
        int updation = ps.executeUpdate();
        fr.close();
        f.delete();
        return updation;
        //the main concept behind hiding is we just copy the data,
        //store in db;
        //then delete the file from the directory,
        //when we hide the file we get it from db and create a new file

    }
    public static void unhideFile(int id)throws SQLException,IOException{
        Connection connection = JdbcConnection.getConnection();
        PreparedStatement ps  = connection.prepareStatement("select path, bin_data from data where id = ?");
        ps.setInt(1,id);
        ResultSet rs = ps.executeQuery();
        rs.next();
        String path = rs.getString("path");
        Clob c = rs.getClob("bin_data");

        Reader r  = c.getCharacterStream();
        FileWriter fw = new FileWriter(path);
        int i ;
        while ((i=r.read()) != -1){
            fw.write((char) i);
        }
        fw.close();
        ps = connection.prepareStatement("delete from data where id = ?");
        ps.setInt( 1,id);
        ps.executeUpdate();
        System.out.println("File Successfully Unhide");

    }

}
