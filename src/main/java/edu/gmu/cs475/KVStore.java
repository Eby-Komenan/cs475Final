package edu.gmu.cs475;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.framework.recipes.nodes.PersistentNode;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.zookeeper.CreateMode;

import java.io.IOException;
import java.rmi.RemoteException;
import java.util.concurrent.ConcurrentHashMap;

public class KVStore extends AbstractKVStore {
	ConcurrentHashMap<String,ConcurrentHashMap<Integer,PersistentNode>> memberships;
	/*
	Do not change these constructors.
	Any code that you need to run when the client starts should go in initClient.
	 */
	public KVStore(String zkConnectString) {
		super(zkConnectString);
	}

	public KVStore(String zkConnectString, int i1, int i2, int i3) {
		super(zkConnectString, i1, i2, i3);
	}



	/**
	 * This callback is invoked once your client has started up and published an RMI endpoint.
	 * <p>
	 * In this callback, you will need to set-up your ZooKeeper connections, and then publish your
	 * RMI endpoint into ZooKeeper (publishing the hostname and port)
	 * <p>
	 * You will also need to set up any listeners to track ZooKeeper events
	 *
	 * CuratorFrameWork is a substitute for the ZooKeeper object
	 * @param localClientHostname Your client's hostname, which other clients will use to contact you
	 * @param localClientPort     Your client's port number, which other clients will use to contact you
	 */
	@SuppressWarnings("resource")
	@Override
	public void initClient(String localClientHostname, int localClientPort) {
		memberships = new ConcurrentHashMap<String,ConcurrentHashMap<Integer,PersistentNode>>();
		// getLocalConnectString() will return string concat of localClientHostname + localClientPort
		PersistentNode znode = new PersistentNode(zk, CreateMode.EPHEMERAL, false, 
				ZK_MEMBERSHIP_NODE + "/" + getLocalConnectString(), new byte[0]);
		// create a leader latch for electing leader
		LeaderLatch elector = new LeaderLatch(zk, ZK_LEADER_NODE + "/" + getLocalConnectString());
		znode.start();
		// if the host name is empty
		if(memberships.get(localClientHostname) == null)
			memberships.put(localClientHostname, new ConcurrentHashMap<Integer,PersistentNode>());
		memberships.get(localClientHostname).put(localClientPort, znode);
		try {
			elector.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Retrieve the value of a key
	 *
	 * @param key
	 * @return The value of the key or null if there is no such key
	 * @throws IOException if this client or the leader is disconnected from ZooKeeper
	 */
	@Override
	public String getValue(String key) throws IOException {
		return null;
	}

	/**
	 * Update the value of a key. After updating the value, this new value will be locally cached.
	 *
	 * @param key
	 * @param value
	 * @throws IOException if this client or the leader is disconnected from ZooKeeper
	 */
	@Override
	public void setValue(String key, String value) throws IOException {

	}

	/**
	 * Request the value of a key. The node requesting this value is expected to cache it for subsequent reads.
	 * <p>
	 * This command should ONLY be called as a request to the leader.
	 *
	 * @param key    The key requested
	 * @param fromID The ID of the client making the request (as returned by AbstractKVStore.getLocalConnectString())
	 * @return The value of the key, or null if there is no value for this key
	 * <p>
	 * DOES NOT throw any exceptions (the RemoteException is thrown by RMI if the connection fails)
	 */
	@Override
	public String getValue(String key, String fromID) throws RemoteException {
		return null;
	}

	/**
	 * Request that the value of a key is updated. The node requesting this update is expected to cache it for subsequent reads.
	 * <p>
	 * This command should ONLY be called as a request to the leader.
	 * <p>
	 * This command must wait for any pending writes on the same key to be completed
	 *
	 * @param key    The key to update
	 * @param value  The new value
	 * @param fromID The ID of the client making the request (as returned by AbstractKVStore.getLocalConnectString())
	 */
	@Override
	public void setValue(String key, String value, String fromID) {

	}

	/**
	 * Instruct a node to invalidate any cache of the specified key.
	 * <p>
	 * This method is called BY the LEADER, targeting each of the clients that has cached this key.
	 *
	 * @param key key to invalidate
	 *            <p>
	 *            DOES NOT throw any exceptions (the RemoteException is thrown by RMI if the connection fails)
	 */
	@Override
	public void invalidateKey(String key) throws RemoteException {

	}

	/**
	 * Called when there is a state change in the connection
	 *
	 * @param client   the client
	 * @param newState the new state
	 */
	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState){
		
	}

	/**
	 * Release any ZooKeeper resources that you setup here
	 * (The connection to ZooKeeper itself is automatically cleaned up for you)
	 */
	@Override
	protected void _cleanup() {

	}
}

