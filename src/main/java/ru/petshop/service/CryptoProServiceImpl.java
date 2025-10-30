package ru.petshop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;

@Service
@Transactional
public class CryptoProServiceImpl implements CryptoProService {

    private static void prepareFiles(String data, String in, String out) throws Exception {
        new PrintWriter(out).close();

        PrintWriter writer = new PrintWriter(in, "UTF-8");
        writer.print(data);
        writer.close();
    }

    private static String crypt(String in, String out) throws Exception {

        ProcessBuilder builder = new ProcessBuilder(
                "csptest",
                "-sfsign",
                "-sign",
                "-in",
                in,
                "-out",
                out,
                "-my",
                "fba982c55cd91951c04890e4e53614e05026b160",
                "-base64",
                "-add"
        );
        builder.redirectErrorStream(true);
        Process p = builder.start();

        new Thread(() -> {
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String line;

            try {
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        p.waitFor();

        try (BufferedReader br = new BufferedReader(new FileReader(out))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String everything = sb.toString();
            everything = everything.replace("\n", "").replace("\r", "");

            return everything;
        }
    }

    @Override
    public String crypt(String data) throws Exception {

        String in = "D:\\Downloads\\111\\telo.txt";
        String out = "D:\\Downloads\\111\\sign_out.txt";

        /*
         /home/nikolay.prokhorov/tmp
        */

        in = "/home/nikolay.prokhorov/tmp/telo.txt";
        out = "/home/nikolay.prokhorov/tmp/sign_out.txt";

        prepareFiles(data, in, out);
        return crypt(in, out);
    }
}