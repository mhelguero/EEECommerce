import React, {useEffect, useState} from 'react';
import axios from 'axios';
import CartItem from '../CartItem/CartItem'; // Adjust the path as needed
import {CartItemType} from '../../types';
import {Wrapper} from './Cart.styles'; // Add your styles here
import {StyledButton} from "../../App.styles.ts";


type Props = {
    addToCart: (clickedItem: CartItemType) => void;
    removeFromCart: (id: number) => void;
    userId: number;
};


const sortByTitle = (items: CartItemType[]) => {
    return items.sort((a, b) => a.title.localeCompare(b.title));
};

const Cart: React.FC = ({addToCart, removeFromCart, userId}) => {
    const [cartItems, setCartItems] = useState<CartItemType[]>([]);

    const fetchCartItems = async () => {
        const url = 'http://localhost:8080/cart';

        try {
            const response = await axios({
                url: url,
                method: 'get',
                headers: {
                    userId: userId,
                    userType: "CUSTOMER",
                },
            });

            console.log('Fetched cart items:', response.data);

            // Map the fetched data to match the CartItemType
            const transformedData = response.data.map((item: any) => ({
                id: item.product.product_id,
                category: item.product.category,
                description: item.product.description,
                image: item.product.image,
                price: item.product.price,
                title: item.product.name,
                amount: item.count,
            }));

            setCartItems(sortByTitle(transformedData));
        } catch (error) {
            console.error('Error fetching cart items:', error);
        }
    };

    useEffect(() => {
        fetchCartItems();
    }, []);

    const handleAddToCart = (clickedItem: CartItemType) => {
        setCartItems((prev) =>
            prev.map((item) =>
                item.id === clickedItem.id ? {...item, amount: item.amount + 1} : item
            )
        );
    };

    const handleRemoveFromCart = (id: number) => {
        setCartItems((prev) =>
            prev.reduce((acc, item) => {
                if (item.id === id) {
                    if (item.amount === 1) return acc;
                    return [...acc, {...item, amount: item.amount - 1}];
                } else {
                    return [...acc, item];
                }
            }, [] as CartItemType[])
        );
    };

    const calculateTotal = (items: CartItemType[]) =>
        items.reduceRight(
            (accumulator: number, item) => {
                console.log(item.discount, (accumulator + item.amount * item.price * (1 - item.discount) / 100).toFixed(2));
                return +((accumulator + item.amount * item.price * (1 - item.discount) / 100).toFixed(2));
            }, 0
        );

    async function handleCheckout() {
        const header = {
            userId: userId
        }
        const response = await axios({
            url: 'http://localhost:8080/orders',
            method: 'post',
            headers: header
        });
    }

    return (
        <Wrapper>
            {cartItems.length > 0 ? (
                cartItems.map((item) => (
                    <CartItem
                        key={item.id}
                        item={item}
                        addToCart={handleAddToCart}
                        removeFromCart={handleRemoveFromCart}
                        fetchCartItems={fetchCartItems} // Pass the function to refresh the cart
                    />
                ))
            ) : (
                <p>No items in cart</p>
            )}
            <h2>Total: ${calculateTotal(cartItems).toFixed(2)}</h2>
            <StyledButton onClick={handleCheckout}>Checkout</StyledButton>
        </Wrapper>
    );
};

export default Cart;
