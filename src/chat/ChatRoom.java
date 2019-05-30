package chat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

import javax.websocket.Session;

public class ChatRoom {

	private final ConcurrentMap<String, Lock>locks = new ConcurrentHashMap<>();
	private final ConcurrentMap<String, List<Session>> sessions = new ConcurrentHashMap<String, List<Session>>() {
        public List<Session> get(final Object key) {
            List<Session> ret = super.get(key);
            if (ret == null) {
                ret = new CopyOnWriteArrayList<>();
                this.put((String) key, ret);
            }
            return ret;
        }
    };
	private String descriptor;
	private String name;

	public ChatRoom(String descriptor, String name) {
		super();
		this.descriptor = descriptor;
		this.name = name;
	}

	public String getDescriptor() {
		return descriptor;
	}

	public void setDescriptor(String descriptor) {
		this.descriptor = descriptor;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

    public void addSession( final Session session) {
        synchronized (this.getLock(this.descriptor)) {
            this.sessions.get(this.descriptor).add(session);
        }
    }

    public List<Session> getSessions() {
        return this.sessions.get(this.descriptor);
    }

    public void removeSession(final Session session) {
        synchronized (this.getLock(this.descriptor)) {
            this.sessions.get(this.descriptor).remove(session);
        }
    }

    public List<String> getRoomDescriptors() {
    	List<String> roomDescriptors = new ArrayList<>();

    	for(String key : sessions.keySet()) {
    		roomDescriptors.add(key);
    	}
    	return roomDescriptors;
    }

    private Lock getLock(final String pRoomDescriptor) {
        final Lock newLock = new Lock();
        final Lock alreadyLock = this.locks.putIfAbsent(pRoomDescriptor, newLock);
        return alreadyLock == null ? newLock : alreadyLock;
    }

    private static class Lock {
        // nodef
    }
}
