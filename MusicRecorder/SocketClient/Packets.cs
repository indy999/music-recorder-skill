using Newtonsoft.Json;
using System;


public enum OpCode{
	Invalid = -1,
	Echo = 0,
	Login = 1,
	LoginResult = 2,
	FindRoom = 3,
	RoomFound = 4,
	ClientLeft = 5,
	BroadcastMessage = 6,
	Error = 7,
    Notification = 8
}

namespace Packets{
	
    [JsonObject]
    public class Intent
    {
        [JsonProperty]
        public string name;
    }

    [JsonObject]
	public class Login{
        [JsonProperty]
        public string userId;
        [JsonProperty]
        public string firstName;
        [JsonProperty]
        public string lastName;
        [JsonProperty]
        public string pictureUrl;
	}

    [JsonObject]
    public class LoginResult{
        [JsonProperty]
        public bool status;
        [JsonProperty]
        public string userId;
        [JsonProperty]
        public string message;
	}
    [JsonObject]
    public class FindRoom{
        [JsonProperty]
        public string name;
        [JsonProperty]
        public int maxPlayers;
	}
    [JsonObject]
    public class ClientInfo{
        [JsonProperty]
        public string userId;
        [JsonProperty]
        public string firstName;
        [JsonProperty]
        public string lastName;
        [JsonProperty]
        public string pictureUrl;
	}
    [JsonObject]
    public class RoomFound{
        [JsonProperty]
        public string roomName;
        [JsonProperty]
        public int roomId;
        [JsonProperty]
        public ClientInfo[] clients;
	}

    [JsonObject]
    public class Error{
        [JsonProperty]
        public string message;
	}
}