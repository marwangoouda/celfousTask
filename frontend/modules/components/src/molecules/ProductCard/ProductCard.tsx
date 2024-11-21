import React from 'react';
import { ProductCardProps } from './types';
import styles from './ProductCard.module.scss';

const ProductCard: React.FC<ProductCardProps> = ({
  image,
  title,
  description,
  price,
  onAddToCart,
}) => {
  return (
    <div className={styles.productCard}>
      <img src={image} alt={title} className={styles.productImage} />
      <h2 className={styles.productTitle}>{title}</h2>
      <p className={styles.productDescription}>{description}</p>
      <p className={styles.productPrice}>{price}€</p>
      <button onClick={onAddToCart} className={styles.addToCartButton}>
        Add to Cart
      </button>
    </div>
  );
};

export default ProductCard;
