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
    request = req(url+resource);
    return await fetch(request);
}

let par = document.querySelector("p")
baseUrl = "http://localhost:8080/api"
fetchData(baseUrl, "/batches").then(data => par.textContent = data.json())

