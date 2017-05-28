using Newtonsoft.Json;
using System;

[JsonObject]
public class Message
{
    [JsonProperty]
	public string name;
}

[JsonObject]
public class Ping : Message {

    [JsonProperty]
    public string message;
}

[JsonObject]
public class Pong : Message {

    [JsonProperty]
    public string message;
}