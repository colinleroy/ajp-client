// Copyright (c) 2010 Espen Wiborg <espenhw@grumblesmurf.org>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package org.grumblesmurf.ajp;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.lang.NumberFormatException;
import java.lang.ArrayIndexOutOfBoundsException;

import java.net.URL;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class AjpCommandLineClient
{

    private static void usage(String errMsg) {
        System.out.println("Usage: java -jar ajp-client.jar [OPTIONS]");
        System.out.println("    --help                : this help");
        System.out.println(" -h --host  [HOST]        : [required] hostname to connect to");
        System.out.println(" -p --port  [PORT]        : [required] port to connect to");
        System.out.println(" -u --url   [URL]         : [optional] URL to fetch");
        System.out.println(" -l --login [LOGIN]       : [optional] Login");
        System.out.println("    --password [PASSWORD] : [optional] Password");
        System.out.println("    --post [FILE]         : [optional] File to post data from");
        System.out.println("    --header [HEADER]     : [optional] Extra request header in the form \"Header: Value\"");
        System.out.println("                                       Multiple extra headers can be sent");

        if (errMsg != null) {
            System.out.println("");
            System.err.println(errMsg);
            System.exit(1);
        }

        System.exit(0);
    }

    public static void main(String[] args) throws Exception {
        int i = 0;
        
        String host = null;
        int port = -1;
        URL url = null;
        String login = null;
        String password = null;
        String postData = null;
        ArrayList<String> extraHeaders = new ArrayList<String>();

        while (i < args.length) {
            try {
                switch(args[i]) {
                    case "--help":
                        usage(null);
                        break;

                    case "-h":
                    case "--host":
                        i++;
                        host = args[i];
                        break;

                    case "-p":
                    case "--port":
                        i++;
                        try {
                            port = Integer.parseInt(args[i]);
                        } catch (NumberFormatException e) {
                            usage("Invalid port "+args[i]);
                        }
                        break;

                    case "-u":
                    case "--url":
                        i++;
                        url = new URL(args[i]);
                        break;
                
                    case "-l":
                    case "--login":
                        i++;
                        login = args[i];
                        break;
                
                    case "--password":
                        i++;
                        password = args[i];
                        break;
                
                    case "--post":
                        i++;
                        postData = args[i];
                        break;

                    case "--header":
                        i++;
                        extraHeaders.add(args[i]);
                        break;

                    default:
                        usage("Unknown option "+args[i]);
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                usage("Missing parameter value after "+ args[i - 1]);
            }
            i++;
        }

        if (host == null) {
            usage("Missing host");
        }
        
        if (port == -1) {
            usage("Missing port");
        }

        AjpClient ac = AjpClient.newInstance(host, port);

        if (url == null) {
            System.out.printf("CPing %s:%s: %s%n", host, port, ac.cping() ? "OK" : "NOK");
        } else {
            if (login != null && password != null) {
                ac.setAuthentication(login, password);
            }
            
            for (String header: extraHeaders) {
                String[] headerParts = header.split(":", 2);
                if (headerParts.length < 2) {
                    usage("Invalid header \"" + header + "\": Required form is \"Name: Value\"");
                } else {
                    ac.addHeader(headerParts[0].trim(), headerParts[1].trim());
                }
            }

            AjpResponse resp;
            if (postData == null)
                resp = ac.get(url);
            else 
                resp = ac.post(url, read(postData));
            
            System.out.printf("%s %s%n", resp.getResponseCode(), resp.getResponseMessage());
            for (Map.Entry<String, List<String>> e : resp.getHeaderFields().entrySet()) {
                System.out.printf("%s: %s%n", e.getKey(), e.getValue());
            }
            System.out.println();
            System.out.print(new String(resp.getContent(), "ISO-8859-1"));
        }
        ac.close();
    }

    private static byte[] read(String filename) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        InputStream in = new FileInputStream(filename);
        byte[] buffer = new byte[2048];
        int read;
        while ((read = in.read(buffer)) != -1) {
            bos.write(buffer, 0, read);
        }
        in.close();
        
        return bos.toByteArray();
    }
}
