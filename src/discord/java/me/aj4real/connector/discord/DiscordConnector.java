package me.aj4real.connector.discord;

import me.aj4real.connector.Connector;
import me.aj4real.connector.Endpoint;
import me.aj4real.connector.Logger;
import me.aj4real.connector.Response;
import me.aj4real.connector.discord.events.DiscordWebSocket;
import me.aj4real.connector.discord.exceptions.BotPermissionsException;
import me.aj4real.connector.discord.exceptions.DiscordRestException;
import me.aj4real.connector.discord.objects.Bot;
import me.aj4real.connector.discord.objects.Role;
import org.json.simple.JSONObject;

import javax.imageio.ImageIO;
import javax.naming.OperationNotSupportedException;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class DiscordConnector extends Connector {
    private final String appId, sessionId;
    private Discord discord;
    public DiscordConnector(String token) {
        this.userAgent = "DiscordBot (www.adriftus.com/, 0.0.1)";
        this.auth = "Bot " + token;
        DiscordWebSocket l = new DiscordWebSocket(this, getHandler(), token);
        l.listen();
        this.appId = l.getAppId();
        this.sessionId = l.getSessionId();
        this.discord = new Discord(this);
        Logger.log(Logger.Level.INFO, "Discord Connector is ready.");
    }

    @Override
    public Response readJson(Endpoint endpoint, String data, Map<String,String> additionalHeaders) throws IOException {
        Response r = super.readJson(endpoint, data, additionalHeaders);
        if(r.getData() instanceof JSONObject) {
            JSONObject json = (JSONObject) r.getData();
            if(json.containsKey("code") && json.containsKey("message")) {
                DiscordRestException ex = new DiscordRestException(this, json);
                //used for handling errors
                long code = (long) json.get("code");
                switch ((int) code) {
                    case 10013:
                        //TODO
                        break;
                    case 50013:
                        for (StackTraceElement stackTraceElement : Thread.currentThread().getStackTrace()) {
                            Class<?> aClass = null;
                            try {
                                aClass = Class.forName(stackTraceElement.getClassName());
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                            String methodName = stackTraceElement.getMethodName().replace("lambda$", "").split("([$])")[0];
                            Method[] methods = aClass.getMethods();
                            for(Method method : methods) {
                                if(method.getName().equals(methodName) && method.getAnnotations().length > 0) {
                                    for (Annotation annotation : method.getAnnotations()) {
                                        if(annotation instanceof RequiresDiscordPermission) {
                                            RequiresDiscordPermission anno = (RequiresDiscordPermission) annotation;
                                            Bot bot = null;
                                            try {
                                                bot = getDiscord().fetchBot();
                                            } catch (Exception err) {
                                                err.printStackTrace();
                                            }
                                            String strPerms = "";
                                            for(int i = 0; i < anno.permission().length; i++) {
                                                if(i != anno.permission().length - 1) {
                                                    strPerms = strPerms + anno.permission()[i] + ", ";
                                                } else {
                                                    strPerms = strPerms + anno.permission()[i];
                                                }
                                            }
                                            strPerms = "[ " + strPerms.trim() + " ]";
                                            BotPermissionsException ex2 = new BotPermissionsException(bot, anno.permission(), strPerms);
                                            ex2.initCause(ex);
                                            throw ex2;
                                            //if this exception is not thrown, there is a method that is not marked with the required permissions.
                                        }
                                    }
                                }
                            }
                        }
                        break;
                }
                throw ex;
            }
        }
        return r;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String getAppId() {
        return this.appId;
    }

    public Discord getDiscord() {
        return this.discord;
    }

    @Override
    public void setAuthenticationBasic(String user, String pass) throws OperationNotSupportedException {
        throw new OperationNotSupportedException("Discord does not support basic password authentication to their API.");
    }

    public static String getImageData(String url) throws IOException {
        BufferedImage image = ImageIO.read(new URL(url));
        if (System.getProperty("java.version").startsWith("11.") || System.getProperty("java.version").startsWith("12."))  {
            // java 11/12 issue: https://stackoverflow.com/questions/54119613/sslhandshakeexception-received-fatal-alert-record-overflow
            image.createGraphics().scale(128, 128);
            BufferedImage tThumbImage = new BufferedImage( 128, 128, BufferedImage.TYPE_INT_RGB );
            Graphics2D tGraphics2D = tThumbImage.createGraphics();
            tGraphics2D.drawImage( image, 0, 0, 128, 128, null );
            image = tThumbImage;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            ImageIO.write(image, "jpg", baos);
        } catch (IOException e) {
            Logger.handle(e);
        }
        String imageString = "data:image/jpg;base64," +
                Base64.getEncoder().encodeToString(baos.toByteArray());
        return imageString;
    }

    public static Date getTimestamp(String strTime) {
        try {
            int year = Integer.valueOf(strTime.substring(0, 4));
            int month = Integer.valueOf(strTime.substring(5, 7));
            int day = Integer.valueOf(strTime.substring(8, 10));
            int hour = Integer.valueOf(strTime.substring(11, 13));
            int minute = Integer.valueOf(strTime.substring(14, 16));
            int second = Integer.valueOf(strTime.substring(17, 19));
            return new Date(year-1900, month, day, hour, minute, second);
        } catch (Exception e) {
            return null;
        }
    }

    public static String getTimestamp(Date date) {
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return formatter.format(date).replace(" ", "T");
    }

}
