import { defineStore } from 'pinia'

export const useDataStore = defineStore('dataStore',{
    state: () => {
        return {
            darkStatus : true,
        }
    },
    getters : {
        getDarkStatus() {
            return this.darkStatus
        },
    },
    actions : {
        changeDarkStatus() {
            this.darkStatus = !this.darkStatus
        },
    }
})