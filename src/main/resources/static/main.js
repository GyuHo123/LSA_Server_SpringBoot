const html_box = document.querySelector(".box");
const html_post_button = document.querySelector(".post-button")
const html_get_button = document.querySelector(".get-button")
// const html_clear_button = document.querySelector(".clear-button")

function pushData(){
    var data = {
        key1: "value1",
        key2: "value2"
    };
    fetch('tlqkf', {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify(data)
    })
    .then(response => response.json())
    .then(data => {
        console.log('Success POST:', data);
        addList(data)
    })
    .catch((error) => {
        console.error('Error POST:', error);
    });
}

function getData() {
    fetch('/item-consume/get-data')
    .then(response => response.json())
    .then(data => {
        console.log('Success GET:', data);
        addList(data)
    })
    .catch(error => console.error('Error GET:', error));
}


function addList(json_data){
    // let list = ""
    // for (let key in json_data) {
    //     if (json_data[key]) {
    //         let value = json_data[key];
    //         list+=`<div><b>${key}</b> ${value}</div>`
    //     }
    // }
    // html_box.innerHTML = list;
    let html_div = document.createElement("div")
    html_div.innerText = JSON.stringify(json_data)
    html_box.append(html_div)
}

function clearList(){
    html_box.innerHTML=""
}

// var myObject = {
//     key1: 'value1',
//     key2: 'value2',
//     key3: 'value3'
// };

for(let i=0; i<10; i++)getData()



//html_post_button.addEventListener("click", pushData);
html_get_button.addEventListener("click", getData);
// html_clear_button.addEventListener("click", clearList);