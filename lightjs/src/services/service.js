
function createRequest(url) {
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

export async function fetchResource(url) {
    const response = await fetch(createRequest(url));
    const data     = await response.json();

    return JSON.parse(JSON.stringify(data));
}

export async function fetchResource(url, resource) {
    return fetchResource(url+resource);
}