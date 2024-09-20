export type UserType = {
  firstName: string;
  lastName: string;
  address: string;
  email: string;
  password: string;
  userType: 'CUSTOMER' | 'EMPLOYEE'; // Adjust this based on the actual enum or string values
};

export type Order = {
  orderId: number;
  user: UserType;
  date: string; 
};

export type Product = {
  product_id: number;
  price: number;
  discount: number;
  name: string;
  image: string;
  description: string;
  quantity: number;
};


export type OrderItem = {
  orderItemId: number;
  order: Order;
  product: Product;
  count: number;
};

export type CartItemType = {
  id: number;
  category: string;
  description: string;
  image: string;
  price: number;
  title: string;
  amount: number;
  discount: number;
};