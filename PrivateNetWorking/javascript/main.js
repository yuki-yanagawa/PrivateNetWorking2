var activeUSerTemplate;
var fileListTemplate;
var url = "";
var websocket;
$(function() {
    $("#join-button").off("click").on("click", join);
    url = location.href;
    console.log("URL: " + url);
    activeUSerTemplate = $("#usertemplate").clone();
    fileListTemplate = $("#filetemplate").clone();
    window.addEventListener("beforeunload", function(event) {
        event.preventDefault();
        alert("You are leaving the page");
    });
    $('#GetFileList').off("click").on('click', function() {
        getFileList();
    });
    $('#GetFile').off("click").on('click', function() {
        getFile();
    });
    $('#Call').off("click").on('click', function() {
        openCallWindow('request');
    });
});

function getFileList() {
    var selectedUser = $("#selectedUser").val().trim();
    if (selectedUser === "") {
        alert("Please select a user.");
        return;
    }
    var addr = selectedUser.split(/\s+/g)[1];
    $('#GetFileList').prop('disabled',true);
    $.ajax({
        type: "POST",
        url: url + "requestDirList",
        data: JSON.stringify({ useraddr: addr }),
        contentType: "application/json",
        success: function(response) {
            // Handle successful response
            debugger;
            console.log("File list retrieved successfully:", response);
            createFileList(response.fileList);
            $('#GetFileList').prop('disabled',false);
        },
        error: function(xhr, status, error) {
            console.error("Error retrieving file list:", error);
            $('#GetFileList').prop('disabled',false);
        }
    });
}

function getFile() {
    var selectedUser = $("#selectedUser").val().trim();
    if (selectedUser === "") {
        alert("Please select a user.");
        return;
    }
    var selectedFile = $("#selectedFile").val().trim();
    if (selectedFile === "") {
        alert("Please select a file.");
        return;
    }
    var addr = selectedUser.split(/\s+/g)[1];
    var fileName = selectedFile;
    $('#GetFile').prop('disabled',true);
    $.ajax({
        type: "POST",
        url: url + "requestFile",
        data: JSON.stringify({ useraddr: addr, filename: fileName }),
        contentType: "application/json",
        success: function(response) {
            // Handle successful response
            console.log("File retrieved successfully:", response);
        },
        error: function(xhr, status, error) {
            console.error("Error retrieving file:", error);
        }
    });
}

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
        target : 'UPDATE_MEMBER, REQUEST_FILE_ACK, REQUEST_CALL,'
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
        console.log("WebSocket message received: " + event.data);
        var data = event.data.split("\r\n");
        if(data[0] ===  'UPDATE_MEMBER') {
            createMembershipList(data[1]);
        }
        if(data[0] === 'REQUEST_FILE_ACK') {
            alert("File transfer completed.");
        }
         if(data[0] === 'REQUEST_CALL') {
            //alert("Call request received.");
            localStorage.setItem("requestUser", data[1].split("CALLREQUEST_NAME=")[1].trim());
            localStorage.setItem("requestUserAddr", data[2].split("CALLREQUEST_ADDR=")[1].trim());
            openCallWindow('receive');
        }
    };
    websocket.onclose = function(event) {
        console.log("WebSocket connection closed");
    };
}


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
            $("#selectedUser").val(this.innerHTML);
            console.log("Selected user: " + this.innerHTML);
        });
    }
}

function createFileList(files) {
    const fileList = $(".pain-files ul");
    fileList.empty();
    for (var i = 0; i < files.length; i++) {
        if (files[i].indexOf("filename=") < 0) {
            continue;
        }
        var datas = files[i].split(":");
        var filename = datas[0].split("filename=")[1].trim();
        var size = datas[1].split("filesize=")[1].trim();
        // if (size >= 1024) {
        //     size = (size / 1024).toFixed(1) + " MB";
        // } else {
        //     size = size + " KB";
        // }
        const fileItem = fileListTemplate.clone();
        fileItem.find(".fileBt").text(filename).show();
        fileItem.attr("id", "file-" + (i + 1));
        fileList.append(fileItem);
        $("#file-" + (i + 1)).find(".fileBt").off("click").on("click", function() {
            $("#selectedFile").val(this.innerHTML);
            console.log("Selected file: " + this.innerHTML);
        });
    }
}


function openCallWindow(mode) {
    if ($("#selectedUser").val().trim() === "") {
        alert("Please select a user.");
        return;
    }
    localStorage.setItem("serverUrl", url);
    localStorage.setItem("selectedUser", $("#selectedUser").val().trim());
    localStorage.setItem("myname", $("#name").val().trim());
    var callFuncURL = url + "callWindow?mode=" + mode;
    console.log("Call Function URL: " + callFuncURL);
    window.open(callFuncURL,"sub window","width=800,height=600");
}

function openCallWindowTest(mode) {
    localStorage.setItem("serverUrl", url);
    localStorage.setItem("selectedUser", $("#selectedUser").val().trim());
    localStorage.setItem("myname", $("#name").val().trim());
    var callFuncURL = url + "callWindow?mode=" + mode;
    window.open(callFuncURL);
}
