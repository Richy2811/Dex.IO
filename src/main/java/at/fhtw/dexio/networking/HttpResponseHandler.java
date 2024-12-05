package at.fhtw.dexio.networking;

import at.fhtw.dexio.exceptions.HttpClientErrorException;
import at.fhtw.dexio.exceptions.HttpHandlerException;
import at.fhtw.dexio.exceptions.HttpRedirectionException;
import at.fhtw.dexio.exceptions.HttpServerErrorException;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpResponseHandler {
    public static String handleResponse(BufferedReader reader) throws IOException {
        //use StringBuilder to create string from server output
        StringBuilder sb = new StringBuilder();

        //check http response status code (first line in response)
        String line = reader.readLine();
        String[] responseCodeParts = line.split(" ");

        //first part is not needed (HTTP version)
        int httpResponseCode = Integer.parseInt(responseCodeParts[1]);
        //check returned response code (response 1xx is not considered to be a possible response if the server does not expect a request body)
        if(httpResponseCode >= 200 && httpResponseCode <= 299){
            //request was successful
            //get the remaining header and body of the response
            while((line = reader.readLine()) != null){
                sb.append(line).append("\r\n");
            }

            //split header and body
            String[] responseParts = sb.toString().split("\r\n\r\n");

            sb.delete(0, sb.length());
            //json content is split in multiple blocks of strings if response was large, alternating between length and
            String[] bodyContent = responseParts[1].split("\r\n");
            for(int i = 1; i < bodyContent.length; i += 2){
                sb.append(bodyContent[i]);
            }
            return sb.toString();
        }
        else if(httpResponseCode >= 300 && httpResponseCode <= 399){
            //redirection response
            throw new HttpRedirectionException(httpResponseCode + " " + responseCodeParts[2]);
        }
        else if(httpResponseCode >= 400 && httpResponseCode <= 499){
            //client error response
            throw new HttpClientErrorException(httpResponseCode + " " + responseCodeParts[2]);
        }
        else if(httpResponseCode >= 500 && httpResponseCode <= 599){
            //server error response
            throw new HttpServerErrorException(httpResponseCode + " " + responseCodeParts[2]);
        }
        else {
            //unknown response
            throw new HttpHandlerException(httpResponseCode + " " + responseCodeParts[2]);
        }
    }
}
