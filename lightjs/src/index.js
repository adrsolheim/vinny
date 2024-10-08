import { config } from "./util/local.js";


function req(url) {
    return new Request(url, {
        method: "GET",
        mode: "cors",
        cache: "no-cache",
        credentials: "same-origin",
        headers: header(),
        redirect: "follow",
        referrerPolicy: "no-referrer"
    });
}

function header() {
    return {
        "Content-Type": "application/json"
    }
}

async function fetchData(url, resource) {
    const request = req(url+resource);
    return fetch(request).then(response => {
        console.log(response.statusText);
        if (response.ok) {
            return JSON.parse(JSON.stringify(response.json()));
        }
        return [];
    })
}


async function populateCategories(categories) {
    const baseUrl = "http://localhost:8080/api/"
    for (let i in categories) {
        let category = categories[i];
        let jsonList = await fetchData(baseUrl, category);
        let div = document.getElementById(category);
        let ul = document.createElement("ul");
        ul.setAttribute("id", `${category}Ul`);
        div.prepend(ul);
        for (element of jsonList) {
            let li = document.createElement("li");
            li.innerHTML = JSON.stringify(element);
            ul.appendChild(li);
        }
    }
}

async function populatePublicData() {
    const baseUrl = "http://localhost:8080/api/"
    const request = req(baseUrl + "public");
    const response = await fetch(request);
    const data = await response.text();
    let h3 = document.getElementById("publicData");
    h3.innerText = data;
}

function createButtons(category) {
    let div = document.getElementById(category);
    let btn = document.createElement("button");
    btn.innerHTML = "Fetch";
    btn.setAttribute("id", `${category}Btn`);
    div.appendChild(btn)
    btn.addEventListener("click", function() {
        let ul = document.getElementById(`${category}Ul`);
        ul.parentElement.removeChild(ul);
        populateCategories([category]);
    });
}

console.log("api key: " + config.apiKey);

populatePublicData();
const categories = ["batches", "recipes", "taphouse"];
populateCategories(categories).then(() => {
    for (const category of categories) {
        createButtons(category);
    }
})