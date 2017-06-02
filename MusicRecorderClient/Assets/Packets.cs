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
	Notify = 8
}

namespace Packets{

	[Serializable]
	public class Intent{
		public string name;
	}

	[Serializable]
	public class Login{
		public string userId;
		public string firstName;
		public string lastName;
		public string pictureUrl;
	}

	[Serializable]
	public class LoginResult{
		public bool status;
		public string userId;
		public string message;
	}

	[Serializable]
	public class FindRoom{
		public string name;
		public int maxPlayers;
	}

	[Serializable]
	public class ClientInfo{
		public string userId;
		public string firstName;
		public string lastName;
		public string pictureUrl;
	}

	[Serializable]
	public class RoomFound{
		public string roomName;
		public int roomId;
		public ClientInfo[] clients;
	}


	[Serializable]
	public class Error{
		public string message;
	}
}