export { fetchResource, batches, recipes, taps }

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

async function fetchResource(url) {
    const response = await fetch(createRequest(url));
    const data     = await response.json();

    return JSON.parse(JSON.stringify(data));
}

//export async function fetchResource(url, resource) {
//    return fetchResource(url+resource);
//}

async function batches() {
    return fetchResource("http://localhost:8080/api/batches");
}
async function recipes() {
    return fetchResource("http://localhost:8080/api/recipes");
}
async function taps() {
    return fetchResource("http://localhost:8080/api/taphouse");
}