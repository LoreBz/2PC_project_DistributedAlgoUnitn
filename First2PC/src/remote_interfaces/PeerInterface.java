/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remote_interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

/**
 *
 * @author Lorenzo
 */
public interface PeerInterface extends Remote {

    public boolean processRequest(String command, Map<String, Object> params) throws RemoteException;
}
