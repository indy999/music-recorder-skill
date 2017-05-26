using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using Alexa.NET.Request;
using Alexa.NET.Request.Type;
using Alexa.NET.Response;
using Amazon.Lambda.Core;

// Assembly attribute to enable the Lambda function's JSON input to be converted into a .NET class.
[assembly: LambdaSerializer(typeof(Amazon.Lambda.Serialization.Json.JsonSerializer))]

namespace MusicRecorder
{
    public class Function
    {
        public SkillResponse IntentHandler(SkillRequest input, ILambdaContext context)
        {
            var requestType = input.GetRequestType();
            
            if (requestType == typeof (IntentRequest))
            {
                var intentRequest = (IntentRequest) input.Request;
                if (intentRequest.Intent == null)
                    return MakeSkillResponse("Sorry, something went wrong", true);

                // switch on the intent name and return the appropriate response
                switch (intentRequest.Intent.Name)
                {
                    case "StartRecording":
                        return MakeSkillResponse("Recording Started", true);
                    case "StopRecording":
                        return MakeSkillResponse("Recording Stopped",true);
                    case "PlaybackRecording":
                        return MakeSkillResponse("Playing back the last recording",true);
                    case "Authors":
                        return MakeSkillResponse("The saxaphone brothers Indy and Mash",true);
                }
            }

           return MakeSkillResponse("Sorry, something went wrong",true);
        }

        private SkillResponse MakeSkillResponse(string outputSpeech,
            bool shouldEndSession,
            string repromptText = "Just say,Music Recorder, Start Recording or Stop Recording or Playback Recording.")
        {
            var response = new ResponseBody
            {
                ShouldEndSession = shouldEndSession,
                OutputSpeech = new PlainTextOutputSpeech { Text = outputSpeech }
            };

            if (repromptText != null)
            {
                response.Reprompt = new Reprompt() { OutputSpeech = new PlainTextOutputSpeech() { Text = repromptText } };
            }

            var skillResponse = new SkillResponse
            {
                Response = response,
                Version = "1.0"
            };
            return skillResponse;
        }
    }
}
