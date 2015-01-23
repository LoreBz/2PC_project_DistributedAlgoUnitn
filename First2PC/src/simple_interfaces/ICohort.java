/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simple_interfaces;

import java.util.ArrayList;

/**
 *
 * @author Lorenzo
 */
public interface ICohort {
    public boolean vote(boolean answer, String vote);
    public boolean runTerminationProtocol();
    public boolean commit();
    public boolean abort();
     
    
}
