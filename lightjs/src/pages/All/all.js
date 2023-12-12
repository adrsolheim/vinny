import { batches } from "/src/services/service.js"




function createTable() {
    let table = document.getElementById("batches");
    batches().then(arr => arr.forEach(batch => {
        console.log(batch)
        let tr = `
        <tr>
            <td>${batch.id}</td>
            <td>${batch.brewfatherId}</td>
            <td>${batch.name}</td>
            <td>${batch.status}</td>
            <td>${batch.tapStatus}</td>
            <td>${batch.packaging}</td>
        </tr>
        `;
        table.innerHTML += tr;
    }
    ))

}

createTable();