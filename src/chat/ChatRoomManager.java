package chat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class ChatRoomManager {

	private final ConcurrentMap<String, Lock>locks = new ConcurrentHashMap<>();
	private final ConcurrentMap<String, ChatRoom> rooms = new ConcurrentHashMap<String, ChatRoom>() {
        public ChatRoom get(final Object key) {
            ChatRoom ret = super.get(key);
            if (ret == null) {
                this.put((String) key, ret);
            }
            return ret;
        }
    };

    public void addRoom(final ChatRoom room) {
        synchronized (this.getLock(room.getDescriptor())) {
            this.rooms.put(room.getDescriptor(), room);
        }
    }

    public ChatRoom getRoom(final String descriptor) {
        return this.rooms.get(descriptor);
    }

    public void removeRoom(final ChatRoom room) {
        synchronized (this.getLock(room.getDescriptor())) {
            this.rooms.remove(room.getDescriptor());
        }
    }

    public List<ChatRoom> getRooms() {
    	List<ChatRoom> rooms = new ArrayList<>();

    	for(String key : this.rooms.keySet()) {
    		rooms.add(this.rooms.get(key));
    	}
    	return rooms;
    }

    public List<String> getRoomDescriptors() {
    	List<String> roomDescriptors = new ArrayList<>();

    	for(String key : rooms.keySet()) {
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
