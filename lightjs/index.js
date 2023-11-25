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
    const response = await fetch(request);
    const data = await response.json();

    const ul = document.getElementById("results")
    const jsonList = JSON.stringify(data);
    const list = JSON.parse(jsonList);
    for (let x in list) {
        let li = document.createElement("li");
        li.innerHTML = JSON.stringify(list[x]);
        ul.append(li);
    }

    return data;
}

baseUrl = "http://localhost:8080/api"
fetchData(baseUrl, "/batches").finally(() => console.log("done!"))


