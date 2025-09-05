import { useState } from 'react';

const API_BASE_URL = '/api';

export interface AddToCartResponse {
  success: boolean;
  message: string;
  isLongMessage: boolean;
}

export const useApi = () => {
  const [loading, setLoading] = useState(false);

  const addToCart = async (productId: string): Promise<AddToCartResponse> => {
    setLoading(true);
    
    try {
      const response = await fetch(`${API_BASE_URL}/cart/add/${productId}`, {
        method: 'POST',
      });

      // THIS IS THE CORRECTED LINE: We read the response as plain text
      const text = await response.text();
      
      if (response.ok) {
        // This logic correctly checks the plain text response
        const isLongMessage = text.includes('*') || text.includes('alternative');
        
        return {
          success: true,
          message: text,
          isLongMessage
        };
      } else {
        throw new Error(`HTTP ${response.status}: ${text}`);
      }
    } catch (error) {
      console.error('Error adding to cart:', error);
      return {
        success: false,
        message: error instanceof Error ? error.message : 'Failed to add item to cart',
        isLongMessage: false
      };
    } finally {
      setLoading(false);
    }
  };

  return {
    addToCart,
    loading
  };
};