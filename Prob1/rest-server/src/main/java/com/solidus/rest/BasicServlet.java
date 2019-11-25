package com.solidus.rest;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import org.apache.commons.io.IOUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class BasicServlet extends HttpServlet {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private static MongoClient mongo;
    private static DB db;
    private static DBCollection dbCol;

    private void setUp() throws UnknownHostException {
        if (BasicServlet.mongo == null) {
            BasicServlet.mongo = new MongoClient(new MongoClientURI(
                    "mongodb://solidus:<password>@cluster0-shard-00-00-fij2m.mongodb.net:27017,cluster0-shard-00-01-fij2m.mongodb.net:27017,cluster0-shard-00-02-fij2m.mongodb.net:27017/test?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true&w=majority"));
            BasicServlet.db = BasicServlet.mongo.getDB("solidus");
            BasicServlet.dbCol = BasicServlet.db.getCollection("sha256");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUp();
        String digest = "";
        JsonObject json = new JsonObject();
        String spl[] = req.getRequestURI().split("/");
        if (spl.length > 0) {
            digest = spl[spl.length - 1];
        }
        DBObject searchQuery = new BasicDBObject("hash", digest);
        DBCursor cursor = BasicServlet.dbCol.find(searchQuery);
        if (cursor.hasNext()) {
            json.addProperty("message", (String) cursor.next().get("message"));
            resp.setContentType("application/JSON");
            resp.setCharacterEncoding("utf-8");
            PrintWriter out = resp.getWriter();
            out.print(json);
            out.flush();
        } else {
            json.addProperty("error_msg", "Message not found");
            resp.setContentType("application/JSON");
            resp.setCharacterEncoding("utf-8");
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Message not found");
            PrintWriter out = resp.getWriter();
            out.print(json);
            out.flush();
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        setUp();
        String jsonString = IOUtils.toString(req.getInputStream());
        JsonObject jObj = JsonParser.parseString(jsonString).getAsJsonObject(); // this parses the json
        String message = jObj.get("message").getAsString();
        String shaHash = "";
        DBObject searchQuery = new BasicDBObject("message", message);
        DBCursor cursor = BasicServlet.dbCol.find(searchQuery);
        if (!cursor.hasNext()) {
            try {
                BasicDBObject document = new BasicDBObject();
                document.put("message", message);
                shaHash = toHexString(getSHA(message));
                document.put("hash", shaHash);
                BasicServlet.dbCol.insert(document);
            } catch (NoSuchAlgorithmException e) {
            }

        }
        else {
            shaHash = (String) cursor.next().get("hash");
        }
        // Write to JSON
        JsonObject json = new JsonObject();
        json.addProperty("digest", shaHash);
        resp.setContentType("application/JSON");
        resp.setCharacterEncoding("utf-8");
        PrintWriter out = resp.getWriter();
        out.print(json);
        out.flush();
    }

    public byte[] getSHA(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        return md.digest(input.getBytes(StandardCharsets.UTF_8));
    }

    public String toHexString(byte[] hash) {
        // Convert byte array into signum representation
        BigInteger number = new BigInteger(1, hash);

        // Convert message digest into hex value
        StringBuilder hexString = new StringBuilder(number.toString(16));

        // Pad with leading zeros
        while (hexString.length() < 32) {
            hexString.insert(0, '0');
        }

        return hexString.toString();
    }
}
