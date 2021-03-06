package de.uniba.wiai.lspi.chord.com;

import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import de.uniba.wiai.lspi.chord.data.Entry;
import de.uniba.wiai.lspi.chord.data.ID;
import de.uniba.wiai.lspi.chord.data.URL;

/**
 * Provides methods which remote nodes can invoke.
 *
 * @author Sven Kaffille
 * @author Karsten Loesing
 * @author Masayuki Higashino
 * @version 1.0.5
 */
@ToString
public abstract class Node {

	@Getter
	@Setter
	protected ID id;
	@Getter
	@Setter
	protected URL url;

	public ID getId() {
		return id;
	}

	public void setId(ID id) {
		this.id = id;
	}

	public URL getUrl() {
		return url;
	}

	public void setUrl(URL url) {
		this.url = url;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof Node))
			return false;
		return ((Node) o).id.equals(id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	/**
	 * Requests a sign of live. This method is invoked by another node which thinks it is this node's successor.
	 *
	 * @throws CommunicationException
	 *             Thrown if an unresolvable communication failure occurs.
	 */
	public abstract void ping() throws CommunicationException;

	/**
	 * Returns the Chord node which is responsible for the given key.
	 *
	 * @param id
	 *            ID for which the successor is searched for.
	 * @return Responsible node.
	 * @throws CommunicationException
	 *             Thrown if an unresolvable communication failure occurs.
	 */
	public abstract Node findSuccessor(ID id) throws CommunicationException;

	/**
	 * Requests this node's predecessor in result[0] and successor list in result[1..length-1]. This method is invoked by another node which thinks it is this
	 * node's predecessor.
	 *
	 * @param potentialPredecessor
	 * @return A list containing the predecessor at first position of the list and the successors in the rest of the list.
	 * @throws CommunicationException
	 *             Thrown if an unresolvable communication failure occurs.
	 */
	public abstract List<Node> notify(Node potentialPredecessor) throws CommunicationException;

	/**
	 * Requests this node's predecessor, successor list and entries.
	 *
	 * @param potentialPredecessor
	 *            Remote node which invokes this method
	 * @return References to predecessor and successors and the entries this node will be responsible for.
	 * @throws CommunicationException
	 */
	public abstract ReferencesAndEntries notifyAndCopyEntries(Node potentialPredecessor) throws CommunicationException;

	/**
	 * Stores the given object under the given ID.
	 *
	 * @param entry
	 * @throws CommunicationException
	 *             Thrown if an unresolvable communication failure occurs.
	 */
	public abstract void insertEntry(Entry entry) throws CommunicationException;

	/**
	 * Inserts replicates of the given entries.
	 *
	 * @param entries
	 *            The entries that are replicated.
	 * @throws CommunicationException
	 *             Thrown if an unresolvable communication failure occurs.
	 */
	public abstract void insertReplicas(Set<Entry> entries) throws CommunicationException;

	/**
	 * Removes the given object from the list stored under the given ID.
	 *
	 * replica 也会删除么
	 * @param entry
	 *            The entry to remove from the dht.
	 * @throws CommunicationException
	 *             Thrown if an unresolvable communication failure occurs.
	 */
	public abstract void removeEntry(Entry entry) throws CommunicationException;

	/**
	 * Removes replicates of the given entries.
	 *
	 * @param sendingNode
	 *            ID of sending node; if entriesToRemove is empty, all replicas with ID smaller than the sending node's ID are removed
	 * @param replicasToRemove
	 *            Replicas to remove; if empty, all replicas with ID smaller than the sending node's ID are removed
	 * @throws CommunicationException
	 *             Thrown if an unresolvable communication failure occurs.
	 */
	public abstract void removeReplicas(ID sendingNode, Set<Entry> replicasToRemove) throws CommunicationException;

	/**
	 * Returns all entries stored under the given ID.
	 * 为什么一个 id 下可以有这么多 entries
	 *
	 * @param id
	 * @return A {@link Set} of entries associated with <code>id</code>.
	 * @throws CommunicationException
	 *             Thrown if an unresolvable communication failure occurs.
	 */
	public abstract Set<Entry> retrieveEntries(ID id) throws CommunicationException;

	/**
	 * Inform a node that its predecessor leaves the network.
	 *
	 * @param predecessor
	 * @throws CommunicationException
	 *             Thrown if an unresolvable communication failure occurs.
	 */
	public abstract void leavesNetwork(Node predecessor) throws CommunicationException;

	/**
	 * Closes the connection to the node.
	 */
	public abstract void disconnect() throws CommunicationException;

}