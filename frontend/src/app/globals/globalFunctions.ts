import { Buffer } from 'buffer';

export const convertToBase64 = (text: string) => Buffer.from(text).toString('base64');
export const convertFromBase64 = (text: string) => Buffer.from(text, 'base64').toString('ascii');
