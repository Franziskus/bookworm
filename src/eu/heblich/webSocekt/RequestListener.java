package eu.heblich.webSocekt;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

/**
 * This class is needed so <code>config.getUserProperties()</code> in
 * #{@link eu.heblich.webSocekt.ServletAwareConfig} does not return <code>null</code>
 * 
 * @author Franz
 *
 */
@WebListener
public class RequestListener implements ServletRequestListener {

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {

    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        ((HttpServletRequest) sre.getServletRequest()).getSession();
    }

}
