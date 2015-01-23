/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple_interfaces;

import java.util.ArrayList;
import java.util.Map;

/**
 *
 * @author Lorenzo
 */
public interface ICoordinator {

    public boolean sendRequest(String command, Map<String, Object> params);

    public boolean commit(String command, Map<String, Object> params);

    public boolean abort(String command, Map<String, Object> params);

}
