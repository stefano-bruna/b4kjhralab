import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IPatent, defaultValue } from 'app/shared/model/patent.model';

export const ACTION_TYPES = {
  FETCH_PATENT_LIST: 'patent/FETCH_PATENT_LIST',
  FETCH_PATENT: 'patent/FETCH_PATENT',
  CREATE_PATENT: 'patent/CREATE_PATENT',
  UPDATE_PATENT: 'patent/UPDATE_PATENT',
  DELETE_PATENT: 'patent/DELETE_PATENT',
  RESET: 'patent/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IPatent>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type PatentState = Readonly<typeof initialState>;

// Reducer

export default (state: PatentState = initialState, action): PatentState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PATENT_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PATENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_PATENT):
    case REQUEST(ACTION_TYPES.UPDATE_PATENT):
    case REQUEST(ACTION_TYPES.DELETE_PATENT):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_PATENT_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PATENT):
    case FAILURE(ACTION_TYPES.CREATE_PATENT):
    case FAILURE(ACTION_TYPES.UPDATE_PATENT):
    case FAILURE(ACTION_TYPES.DELETE_PATENT):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_PATENT_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_PATENT):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_PATENT):
    case SUCCESS(ACTION_TYPES.UPDATE_PATENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_PATENT):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/patents';

// Actions

export const getEntities: ICrudGetAllAction<IPatent> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PATENT_LIST,
  payload: axios.get<IPatent>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IPatent> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PATENT,
    payload: axios.get<IPatent>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IPatent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PATENT,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IPatent> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PATENT,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IPatent> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PATENT,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
