const express = require('express')
const app = express()
const request = require('request')

app.get('/pokemon/:sum', (req, res) => {
    poke_sum = req.params.sum
    if (poke_sum > 20) {
        poke_sum = 20
    }
    request.get("https://pokeapi.co/api/v2/pokemon/?offset=0&limit=" + poke_sum, (error, response, body) => {
        if (error) {
            return console.dir(error)
        }
        var result = JSON.parse(body)
        res.json(result)
    })


})

app.get('/', (req, res) => {
    res.send("API Status: Active")
})


app.listen(8001, () => {
    console.log("Start Sever at Port:8001")
})