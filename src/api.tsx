import { Product, CartItemType } from "./types";

export const getOrderItems = async (): Promise<CartItemType[]> => {
  const response = await fetch("http://localhost:8080/products");
  if (!response.ok) {
    throw new Error("Network response was not ok");
  }
  const data: Product[] = await response.json();
  data.forEach(product => console.log('Product:', product));

  // Transform OrderItemDTO to CartItemType
  const transformedData: CartItemType[] = data.map((product) => ({
    id: product.product_id,
    category: "default", 
    description: product.description, 
    image: "https://imgs.search.brave.com/t23UE44SC5DumVfc6QuE5TnQfGwWqfW7bNrYVPVpY7o/rs:fit:500:0:0:0/g:ce/aHR0cHM6Ly9tZWRp/YS5pc3RvY2twaG90/by5jb20vaWQvMTYx/NzUxNTA4MS9waG90/by9nbGFzcy1zYWx0/LXNoYWtlci1vbi10/aGUtdGFibGUuanBn/P3M9NjEyeDYxMiZ3/PTAmaz0yMCZjPXoz/aWxsVHFmREZhYU14/dGVwSDJWZXZiTzJ2/X3REeEJKUWFhSWZs/bmV5VWM9", // Set appropriate default or fetched values
    price: product.price, 
    title: product.name, 
    amount: product.quantity,
  }));
  
  return transformedData;
};