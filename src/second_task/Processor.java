package second_task;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;


/**
 * Processor of HTTP request.
 */
public class Processor implements Runnable{
    private final Socket socket;
    private final HttpRequest request;

    public Processor(Socket socket, HttpRequest request) {
        this.socket = socket;
        this.request = request;
    }

    public void number_of_words() throws IOException {
        File file = new File("../DistributedComputing/src/second_task/example.txt");
        try(Scanner sc = new Scanner(new FileInputStream(file))){
            int count=0;
            while(sc.hasNext()){
                sc.next();
                count++;
            }
            System.out.println("Number of words: " + count);
        }
    }

    public void process() throws IOException {
        // Print request that we received.
        System.out.println("Got request:");
        System.out.println(request.toString());
        System.out.flush();
        PrintWriter output = new PrintWriter(socket.getOutputStream());

        //different response for different requests
        if (request.getRequestLine().toString().equals("GET /create/item1 HTTP/1.1")) {
            number_of_words();
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();
            output.println("<html>");
            output.println("<head><title>Creation of a new Item</title></head>");
            output.println("<body><p>Item Created</p></body>");
            output.println("</html>");
            output.flush();
            socket.close();
        }

        else if (request.getRequestLine().toString().equals("GET /delete/item1 HTTP/1.1")) {
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();
            output.println("<html>");
            output.println("<head><title>Deleting the Item</title></head>");
            output.println("<body><p>Item Deleted</p></body>");
            output.println("</html>");
            output.flush();
            socket.close();
        }

        else if (request.getRequestLine().toString().equals("GET /exec/params1 HTTP/1.1")) {
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();
            output.println("<html>");
            output.println("<head><title>Execution of a Parameters</title></head>");
            output.println("<body><p>Parameters executed</p></body>");
            output.println("</html>");
            output.flush();
            socket.close();
        }

        else {
            // We are returning a simple web page now.
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/html; charset=utf-8");
            output.println();
            output.println("<html>");
            output.println("<head><title>Hello</title></head>");
            output.println("<body><p>Hello, world!</p></body>");
            output.println("</html>");
            output.flush();
            socket.close();
        }
    }

    @Override
    public void run() {
        try {
            process();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
