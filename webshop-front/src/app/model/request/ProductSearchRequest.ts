import {AttributeNameValue} from './AttributeNameValue';

export interface ProductSearchRequest {
  nameSearch: string;
  categoryIdSearch: any;
  attributeNameValueSearches: AttributeNameValue[];
}
