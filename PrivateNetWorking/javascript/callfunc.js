var parentUrl = "";
var targetUser = "";
var targetUserAddr = "";
var myname = "";
var websocketForCall = null;
var videoFrame = null;
$(function() {
    if(location.href.split("?").length <= 1) {
        alert("This page cannot be opened directly.");
        window.close();
    }
    var params = new URLSearchParams(location.href.split("?")[1]);
    var mode = params.get("mode");
    videoFrame = document.querySelector("video#camera");
    if(mode === 'request') {
        parentUrl = localStorage.getItem("serverUrl");
        targetUser = localStorage.getItem("selectedUser");
        myname = localStorage.getItem("myname");
        console.log("Removed selected user from local storage: " + targetUser);
        if (targetUser == null || targetUser.trim() === "") {
            window.close();
        }
        callConnectSetting();
        //callConnectTest();
    } else if(mode === 'receive') {
        targetUser = localStorage.getItem("requestUser");
        targetUserAddr = localStorage.getItem("requestUserAddr");
        if (targetUser == null || targetUser.trim() === "" || targetUserAddr == null || targetUserAddr.trim() === "") {
            window.close();
        }
        $("#requestCallUser").val("Call from: " + targetUser).show();
        $('#accept').show().off("click").on("click", function() {
            callConnectSettingForAccept();
            $(this).hide();
        });
    } else {
        alert("Invalid mode.");
        window.close();
    }
});

function callConnectTest() {
    websocketForCall = new WebSocket("ws://" + location.host + "/connectCallSetting");
    websocketForCall.onopen = function(event) {
        console.log("WebSocket connection established");
        navigator.mediaDevices.getUserMedia({ audio: true, video: false })
        .then(function(stream) {
            videoFrame.srcObject = stream;
            const mediaRecorder = new MediaRecorder(stream);
            mediaRecorder.ondataavailable = function(e) {
                // Handle the recorded audio data here
                const audioData = e.data;
                const videoData = new Uint8Array(e.data);
                console.log("videoData = " + videoData);
                websocketForCall.send(videoData);
            };
            mediaRecorder.start(1000);
        });
    };
    websocketForCall.onmessage = function(e) {
        console.log("recive videoData = " + e.data);
        const blob = new Blob([e.data], { type: 'video/webm' });
        const url = URL.createObjectURL(blob);
        videoFrame.src = url;
        videoFrame.play();
    };
    websocketForCall.onclose = function(event) {
        console.log("WebSocket connection closed");
    };
}

function callConnectSettingForAccept() {
    websocketForCall = new WebSocket("ws://" + location.host + "/connectCallSetting");
    websocketForCall.onopen = function(event) {
        console.log("WebSocket connection established");
        navigator.mediaDevices.getUserMedia({ audio: true, video: false })
        .then(function(stream) {
            return new Promise(function(resolve) {
                var Obj = new Object();
                Obj.requestUserAddr = targetUserAddr;
                $.ajax({
                    url: parentUrl + "acceptCall",
                    type: "POST",
                    data: JSON.stringify(Obj),
                    success: function() {
                        resolve(stream);
                    },
                    error: function(err) {
                        console.error("Error setting call media.", err);
                        alert("Could not request call. The user might be offline.");
                        window.close();
                    }
                });
            });
        })
        .then(function(stream) {
            videoFrame.srcObject = stream;
            const mediaRecorder = new MediaRecorder(stream);
            mediaRecorder.ondataavailable = function(e) {
                // Handle the recorded audio data here
                const audioData = e.data;
                // console.log("Audio data available:", audioData);
                // console.log(e);
                const videoData = new Uint8Array(e.data);
                console.log("videoData = " + videoData);
                websocketForCall.send(videoData);
                // const blob = new Blob([videoData], { type: 'video/webm' });
                // const url = URL.createObjectURL(blob);
                // videoFrame.src = url;
                // videoFrame.play();
            };
            mediaRecorder.start(1000);
        })
        .catch(function(err) {
            console.error("Error accessing media devices.", err);
            alert("Could not access camera and microphone. Please check permissions.");
            window.close();
        });
    };

    websocketForCall.onmessage = function(e) {
        console.log("recive videoData = " + e.data);
        const blob = new Blob([e.data], { type: 'video/webm' });
        const url = URL.createObjectURL(blob);
        videoFrame.src = url;
        videoFrame.play();
    };

    websocketForCall.onclose = function(event) {
        console.log("WebSocket connection closed");
    };
}

function callConnectSetting() {
    websocketForCall = new WebSocket("ws://" + location.host + "/connectCallSetting");
    websocketForCall.onopen = function(event) {
        console.log("WebSocket connection established");
        navigator.mediaDevices.getUserMedia({ audio: true, video: false })
        .then(function(stream) {
            return new Promise(function(resolve) {
                var Obj = new Object();
                Obj.targetUser = targetUser.split(/\s+/g)[1];
                Obj.myname = myname;
                $.ajax({
                    url: parentUrl + "requestCall",
                    type: "POST",
                    data: JSON.stringify(Obj),
                    success: function() {
                        resolve(stream);
                    },
                    error: function(err) {
                        console.error("Error setting call media.", err);
                        alert("Could not request call. The user might be offline.");
                        window.close();
                    }
                });
            });
        })
        .then(function(stream) {
            videoFrame.srcObject = stream;
            const mediaRecorder = new MediaRecorder(stream);
            mediaRecorder.ondataavailable = function(e) {
                // Handle the recorded audio data here
                const audioData = e.data;
                // console.log("Audio data available:", audioData);
                // console.log(e);
                const videoData = new Uint8Array(e.data);
                console.log("videoData = " + videoData);
                websocketForCall.send(videoData);
                // const blob = new Blob([videoData], { type: 'video/webm' });
                // const url = URL.createObjectURL(blob);
                // videoFrame.src = url;
                // videoFrame.play();
            };
            mediaRecorder.start(1000);
        })
        .catch(function(err) {
            console.error("Error accessing media devices.", err);
            alert("Could not access camera and microphone. Please check permissions.");
            window.close();
        });
    };

    websocketForCall.onmessage = function(e) {
        console.log("recive videoData = " + e.data);
        const blob = new Blob([e.data], { type: 'video/webm' });
        const url = URL.createObjectURL(blob);
        videoFrame.src = url;
        videoFrame.play();
    };

    websocketForCall.onclose = function(event) {
        console.log("WebSocket connection closed");
    };
}


function videoSetting() {
    const video = document.querySelector("video#camera");
    navigator.mediaDevices.getUserMedia({ audio: true, video: false })
    .then(function(stream) {
        video.srcObject = stream;
        const mediaRecorder = new MediaRecorder(stream);
        mediaRecorder.ondataavailable = function(e) {
            // Handle the recorded audio data here
            const audioData = e.data;
            console.log("Audio data available:", audioData);
            console.log(e);
            const videoData = new Uint8Array(e.data);
            const blob = new Blob([videoData], { type: 'video/webm' });
            const url = URL.createObjectURL(blob);
            video.src = url;
            video.play();
        }
        mediaRecorder.start(1000);
    })
    .catch(function(err) {
        console.error("Error accessing media devices.", err);
        alert("Could not access camera and microphone. Please check permissions.");
        window.close();
    });
}