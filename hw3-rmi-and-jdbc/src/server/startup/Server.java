package server.startup;

import common.FileCatalog;
import server.controller.Controller;
import server.integration.FileCatalogDBException;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {

    private String fileCatalogName = FileCatalog.FILE_CATALOG_NAME_IN_REGISTRY;

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.startRMIServant();
            System.out.println("Server started.....");
        } catch (RemoteException | MalformedURLException | FileCatalogDBException ex) {
            ex.printStackTrace();
        }
    }

    private void startRMIServant() throws RemoteException, MalformedURLException, FileCatalogDBException {
        try {
            LocateRegistry.getRegistry().list();
        } catch (RemoteException noRegistryRunning) {
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }
        Controller controller = new Controller();
        Naming.rebind(fileCatalogName, controller);
    }

}
