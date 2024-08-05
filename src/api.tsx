import { OrderItemType } from "./types";

export const getOrderItems = async (): Promise<OrderItemType> =>{
    const response = await fetch("http://localhost:8080/api/orderitems");
    if(!response.ok){
        throw new Error("could not fetch data from /orderitems")
    }

    const data = await response.json();
    return data;
}