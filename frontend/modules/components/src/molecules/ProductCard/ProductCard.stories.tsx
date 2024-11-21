import React from 'react';
import { ComponentStory, Meta } from '@storybook/react';
import ProductCard from './ProductCard';
import { ProductCardProps } from './types';
import './ProductCard.module.scss';

export default {
  title: 'Components/ProductCard',
  component: ProductCard,
} as Meta<typeof ProductCard>;

const Template: ComponentStory<typeof ProductCard> = (
  args: ProductCardProps
) => <ProductCard {...args} />;

export const Default = Template.bind({});
Default.args = {
  image: 'https://picsum.photos/200/300',
  title: 'Sample Product',
  description: 'This is a sample product description.',
  price: 29.99,
  onAddToCart: () => alert('Added to cart!'),
};

export const WithLongDescription = Template.bind({});
WithLongDescription.args = {
  image: 'https://picsum.photos/200/301',
  title: 'Sample Product with Long Description',
  description:
    'This is a sample product description that is significantly longer than the default description to test how the component handles longer text.',
  price: 49.99,
  onAddToCart: () => alert('Added to cart!'),
};
