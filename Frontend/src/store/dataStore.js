import { defineStore } from 'pinia'

export const useDataStore = defineStore('dataStore',{
    state: () => {
        return {
            // serverAddress: 'https://panel.gixmetir.online:5001/api/v1',
            serverAddress: 'https://panel.privado-vpn.online:5001/api/v1',
            darkStatus : true,
            token : ''
        }
    },
    getters : {
        getDarkStatus() {
            return this.darkStatus
        },
        getServerAddress() {
            return this.serverAddress
        },
        getToken() {
            return this.token
        },
    },
    actions : {
        changeDarkStatus() {
            this.darkStatus = !this.darkStatus
        },
        setDarkStatus(payload) {
            this.darkStatus = payload
        },
        setToken(payload) {
            this.token = `Bearer ${payload}`
        },
    }
})
