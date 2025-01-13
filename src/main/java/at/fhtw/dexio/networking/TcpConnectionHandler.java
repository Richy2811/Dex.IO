package at.fhtw.dexio.networking;

import at.fhtw.dexio.exceptions.HttpClientErrorException;
import at.fhtw.dexio.exceptions.HttpHandlerException;
import at.fhtw.dexio.exceptions.HttpRedirectionException;
import at.fhtw.dexio.exceptions.HttpServerErrorException;

import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;

/**
 * Establishes a connection with the given URL and returns the response
 * body as a string.
 */
public class TcpConnectionHandler {
    public static String getFromUrl(String url){
        //split url into hostname and file path
        String[] urlParts = url.replaceAll("(http|https)://", "").split("/", 2);
        String hostName = urlParts[0];
        String path = "/" + urlParts[1];
        //server only responds to https request
        try(Socket socket = SSLSocketFactory.getDefault().createSocket(hostName, 443)){
            //create output stream to write request to server
            OutputStream os = socket.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
            writer.write("GET " + path + " HTTP/1.1");
            writer.newLine();
            writer.write("Host: " + hostName);
            writer.newLine();
            writer.write("Connection: close");
            writer.newLine();
            writer.newLine();
            writer.flush();

            //create input stream to read server response from
            InputStream in = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            return HttpResponseHandler.handleResponse(reader);
        }
        catch(IOException e){
            System.err.println(e.getMessage()); //TODO Handle exception in meaningful way
            return null;
        }
        catch (HttpRedirectionException e){
            System.err.println(e.getMessage()); //TODO Handle exception in meaningful way
            return null;
        }
        catch (HttpClientErrorException e){
            System.err.println(e.getMessage()); //TODO Handle exception in meaningful way
            return null;
        }
        catch (HttpServerErrorException e){
            System.err.println(e.getMessage()); //TODO Handle exception in meaningful way
            return null;
        }
        catch (HttpHandlerException e){
            System.err.println(e.getMessage()); //TODO Handle exception in meaningful way
            return null;
        }
    }
}
