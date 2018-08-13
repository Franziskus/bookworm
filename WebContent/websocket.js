window.onload = init;
var socket = new WebSocket("ws://localhost:8080/bookworm/actions");
socket.onmessage = onMessage;

function onMessage(event) {
    var device = JSON.parse(event.data);
    if (device.action === "add") {
        printDeviceElement(device);
    }
    if (device.action === "remove") {
        document.getElementById(device.id).remove();
        //device.parentNode.removeChild(device);
    }
    if (device.action === "toggleS") {
        var node = document.getElementById(device.id);
        var statusText = node.children[1];
        statusText.innerHTML = "<b>Status:</b> " + device.status;
    }
    if (device.action === "toggleFP") {
        var node = document.getElementById(device.id);
        var deviceProgress = node.children[2];
        deviceProgress.innerHTML = "<b>Progress:</b> " + device.progress;
        var node1 = document.getElementById("part"+device.id);
        node1.setAttribute("value", device.progress);
        var node2 = document.getElementById("full"+device.id);
        node2.setAttribute("value", (device.progress * 50 / parseInt(node1.getAttribute("max"))));
    }
    if (device.action === "toggleFS") {
        var node = document.getElementById(device.id);
        var deviceProgress = node.children[3];
        deviceProgress.innerHTML = "<b>Size:</b> " + device.size;
        var node1 = document.getElementById("part"+device.id);
        node1.setAttribute("max", device.size);
    }
    if (device.action === "toggleWP") {
        var node = document.getElementById(device.id);
        var deviceProgress = node.children[4];
        deviceProgress.innerHTML = "<b>words Processed:</b> " + device.words;
        var node1 = document.getElementById("part"+device.id);
        node1.setAttribute("value", device.words);
        var node2 = document.getElementById("full"+device.id);
        node2.setAttribute("value", 50 + (device.words * 50 / parseInt(node1.getAttribute("max"))));
    }
    if (device.action === "toggleWS") {
        var node = document.getElementById(device.id);
        var deviceProgress = node.children[5];
        deviceProgress.innerHTML = "<b>words total:</b> " + device.maxWords;
        var node1 = document.getElementById("part"+device.id);
        node1.setAttribute("max", device.maxWords);
    }
}

function addDevice(name, type, description) {
    var DeviceAction = {
        action: "add",
        name: name,
        type: type,
        description: description
    };
    socket.send(JSON.stringify(DeviceAction));
}

function removeDevice(element) {
    var id = element;
    var DeviceAction = {
        action: "remove",
        id: id
    };
    socket.send(JSON.stringify(DeviceAction));
}

function toggleDevice(element) {
    var id = element;
    var DeviceAction = {
        action: "toggle",
        id: id
    };
    socket.send(JSON.stringify(DeviceAction));
}

function printDeviceElement(device) {
    var content = document.getElementById("content");
    
    var deviceDiv = document.createElement("div");
    deviceDiv.setAttribute("id", device.id);
    deviceDiv.setAttribute("class", "device " + device.status);
    content.appendChild(deviceDiv);

    var deviceName = document.createElement("span");
    deviceName.setAttribute("class", "FileName");
    deviceName.innerHTML = "Name "+device.name;
    deviceDiv.appendChild(deviceName);
    
    var deviceStatus = document.createElement("span");
    deviceStatus.setAttribute("class", "status");
    deviceStatus.innerHTML = "<b>Status:</b> " + device.status;
    deviceDiv.appendChild(deviceStatus);

    var deviceProgress = document.createElement("span");
    deviceProgress.setAttribute("class", "progress");
    deviceProgress.innerHTML = "<b>Progress:</b> " + device.progress;
    deviceDiv.appendChild(deviceProgress);
    
    var deviceSize = document.createElement("span");
    deviceSize.setAttribute("class", "Size");
    deviceSize.innerHTML = "<b>Size:</b> " + device.size;
    deviceDiv.appendChild(deviceSize);
    
    var deviceWordProgress = document.createElement("span");
    deviceWordProgress.setAttribute("class", "word");
    deviceWordProgress.innerHTML = "<b>words processed:</b> " + device.words;
    deviceDiv.appendChild(deviceWordProgress);
    
    var deviceWordTotal = document.createElement("span");
    deviceWordTotal.setAttribute("class", "word total");
    deviceWordTotal.innerHTML = "<b>words total:</b> " + device.maxWords;
    deviceDiv.appendChild(deviceWordTotal);

    var deviceFilename = document.createElement("span");
    deviceFilename.innerHTML = "<b>filename:</b> " + device.filename;
    //if (device.status === "On") {
    //    deviceStatus.innerHTML = "<b>Status:</b> " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn off</a>)";
    //} else if (device.status === "Off") {
    //    deviceStatus.innerHTML = "<b>Status:</b> " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn on</a>)";
        //deviceDiv.setAttribute("class", "device off");
    //}
    deviceDiv.appendChild(deviceFilename);

    var deviceDescription = document.createElement("span");
    deviceDescription.innerHTML = "<b>md5:</b> " + device.md5;
    deviceDiv.appendChild(deviceDescription);
    
    var deviceBar1 = document.createElement("progress");
    deviceBar1.setAttribute("id", "part"+device.id);
    if(device.size == "?" && device.maxWords == "?"){
    	deviceBar1.setAttribute("max", "100");
    	deviceBar1.setAttribute("value", "0");
    }else if(device.maxWords != "?"){
    	deviceBar1.setAttribute("max", device.maxWords);
    	deviceBar1.setAttribute("value", device.words);
    }else{
    	deviceBar1.setAttribute("max", device.sizes);
    	deviceBar1.setAttribute("value", device.progress);
    }
    deviceDiv.appendChild(deviceBar1);
    
    var br = document.createElement("br");
    deviceDiv.appendChild(br);
    
    var deviceBar2 = document.createElement("progress");
    deviceBar2.setAttribute("id", "full"+device.id);
    deviceBar2.setAttribute("value", "0");
    deviceBar2.setAttribute("max", "100");
    deviceDiv.appendChild(deviceBar2);

 //   var removeDevice = document.createElement("span");
 //   removeDevice.setAttribute("class", "removeDevice");
 //   removeDevice.innerHTML = "<a href=\"#\" OnClick=removeDevice(" + device.id + ")>Remove device</a>";
 //   deviceDiv.appendChild(removeDevice);
}

/*
function showForm() {
    document.getElementById("addDeviceForm").style.display = '';
}

function hideForm() {
    document.getElementById("addDeviceForm").style.display = "none";
}

function formSubmit() {
    var form = document.getElementById("addDeviceForm");
    var name = form.elements["device_name"].value;
    var type = form.elements["device_type"].value;
    var description = form.elements["device_description"].value;
    hideForm();
    document.getElementById("addDeviceForm").reset();
    addDevice(name, type, description);
}
*/
function init() {
    //hideForm();
}
