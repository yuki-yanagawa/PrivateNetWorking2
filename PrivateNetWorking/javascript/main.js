var activeUSerTemplate;
var url = "";
var websocket;
$(function() {
    $("#join-button").off("click").on("click", join);
    url = location.href;
    console.log("URL: " + url);
    activeUSerTemplate = $("#usertemplate").clone();
    window.addEventListener("beforeunload", function(event) {
        event.preventDefault();
        alert("You are leaving the page");
    });
});

function join() {
    if ($("#name").val().trim() === "") {
        alert("Please enter your name.");
        return;
    }
    const name = $("#name").val().trim();
    var obj = {
        name: name
    };
    $.ajax({
        type: "POST",
        url: url + "join",
        data: JSON.stringify(obj),
        contentType: "application/json",
        success: function(response) {
            if(response.JOIN_ACK === 'OK') {
                 $("#join-button").prop("disabled", true);
                 //pollingWorker();
                 alert("Successfully joined the network!");
                 connectWebSocket();
            }
        },
        error: function(xhr, status, error) {
            alert("Error joining the network: " + error);
        }
    });
}

function registerObservation() {
    var obj = {
        target : 'UPDATE_MEMBER,'
    };
    $.ajax({
        type: "POST",
        url: url + "registerObservation",
        data: JSON.stringify(obj),
        contentType: "application/json",
        success: function(response) {
            console.log("Successfully registered for updates");
            // Handle successful observation request
            // createMembershipList(response.UPDATE_MEMBER.users);
        },
        error: function(xhr, status, error) {
            console.error("Error observing: " + error);
         }
    });
}

function connectWebSocket() {
    websocket = new WebSocket("ws://" + location.host + "/connectWebSocket");
    websocket.onopen = function(event) {
        console.log("WebSocket connection established");
        registerObservation();
    };
    websocket.onmessage = function(event) {
        var data = event.data.split("\r\n");
        if(data[0] ===  'UPDATE_MEMBER') {
            createMembershipList(data[1]);
        }
    };
    websocket.onclose = function(event) {
        console.log("WebSocket connection closed");
    };
}

// function pollingWorker() {
//     setInterval(function() {
//         $.ajax({
//             type: "POST",
//             url: url + "updateMembership",
//             success: function(response) {
//                 createMembershipList(response.users);
//             },
//             error: function(xhr, status, error) {
//                 console.error("Error fetching status: " + error);
//             }
//         });
//     }, 5000);
// }

function createMembershipList(users) {
    const userList = $(".pain-users ul");
    userList.empty();
    var strIndex = users.indexOf("=");
    var usersData = users.substring(strIndex + 1).split(",");
    for(var i = 0; i < usersData.length; i++) {
        if(usersData[i].indexOf("username:") < 0) {
            continue;
        }
        var data = usersData[i].split("username:")[1].trim();
        const userItem = activeUSerTemplate.clone();
        userItem.find(".selectBt").text(data).show();
        userItem.attr("id", "user-" + (i + 1));
        userList.append(userItem);
        $("#user-" + (i + 1)).find(".selectBt").off("click").on("click", function() {
            $("#selectedUser").val(data);
            console.log("Selected user: " + data);
        });
    }
}
