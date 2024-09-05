import { Product, CartItemType } from "./types";

export const getProducts = async (): Promise<CartItemType[]> => {
  const response = await fetch("http://3.141.164.75:8080/products");
  if (!response.ok) {
    throw new Error("Network response was not ok");
  }
  const data: Product[] = await response.json();
  data.forEach(product => console.log('Product:', product));

  // Transform Product to CartItemType
  const transformedData: CartItemType[] = data.map((product) => ({
    id: product.product_id,
    category: "default", 
    description: product.description, 
    image: product.image, 
    price: product.price, 
    title: product.name, 
    amount: product.quantity,
    discount: product.discount,
  }));

  console.log(transformedData);
  return transformedData;
};