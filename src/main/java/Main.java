import accountService.AccountService;
import frontend.Frontend;
import gameMech.GameMech;
import message.MessageService;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import utils.VFS.VFS;
import utils.resources.Connection;
import utils.resources.Resources;
import java.util.Iterator;

/*
 * oppa google style
 */
public class Main {
    public static Server buildServer(Integer port, Connection connParams) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        MessageService ms = new MessageService();
        AccountService accountServices[] = {new AccountService(ms, connParams), new AccountService(ms, connParams)};
        Frontend frontend = new Frontend(ms);
        GameMech gameMech = new GameMech(ms);

        // Warn: start threads after init all of subsystems
                (new Thread(frontend)).start();
        (new Thread(accountServices[0])).start();
        (new Thread(accountServices[1])).start();
        (new Thread(gameMech)).start();

        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.addServlet(new ServletHolder(frontend), "/*");

        ResourceHandler resource_handler = new ResourceHandler();
        resource_handler.setResourceBase("static");

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resource_handler, context});
        server.setHandler(handlers);
        return server;
    }

    public static void main(String[] args) throws Exception {
        VFS vfs = new VFS("");
        Iterator<String> files = vfs.getIterator("data");
        Resources resources = utils.resources.Resources.getInstance();
        String nextFile;
        while (files.hasNext()){
            nextFile = files.next();
            if (vfs.isFile(nextFile)){
                resources.addResource(nextFile);
            }
        }

        Connection con = (Connection)resources.getResource("data/connection.xml");
        Server server = buildServer(con.getPORT(), con);
        server.start();
        server.join();
    }

}
