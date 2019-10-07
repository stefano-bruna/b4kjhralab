import React from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './patent.reducer';
import { IPatent } from 'app/shared/model/patent.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IPatentProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export class Patent extends React.Component<IPatentProps> {
  componentDidMount() {
    this.props.getEntities();
  }

  render() {
    const { patentList, match } = this.props;
    return (
      <div>
        <h2 id="patent-heading">
          <Translate contentKey="b4KjhralabApp.patent.home.title">Patents</Translate>
          <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="b4KjhralabApp.patent.home.createLabel">Create a new Patent</Translate>
          </Link>
        </h2>
        <div className="table-responsive">
          {patentList && patentList.length > 0 ? (
            <Table responsive aria-describedby="patent-heading">
              <thead>
                <tr>
                  <th>
                    <Translate contentKey="global.field.id">ID</Translate>
                  </th>
                  <th>
                    <Translate contentKey="b4KjhralabApp.patent.uid">Uid</Translate>
                  </th>
                  <th>
                    <Translate contentKey="b4KjhralabApp.patent.name">Name</Translate>
                  </th>
                  <th>
                    <Translate contentKey="b4KjhralabApp.patent.description">Description</Translate>
                  </th>
                  <th>
                    <Translate contentKey="b4KjhralabApp.patent.client">Client</Translate>
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {patentList.map((patent, i) => (
                  <tr key={`entity-${i}`}>
                    <td>
                      <Button tag={Link} to={`${match.url}/${patent.id}`} color="link" size="sm">
                        {patent.id}
                      </Button>
                    </td>
                    <td>{patent.uid}</td>
                    <td>{patent.name}</td>
                    <td>{patent.description}</td>
                    <td>{patent.client ? <Link to={`client/${patent.client.id}`}>{patent.client.id}</Link> : ''}</td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button tag={Link} to={`${match.url}/${patent.id}`} color="info" size="sm">
                          <FontAwesomeIcon icon="eye" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.view">View</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${patent.id}/edit`} color="primary" size="sm">
                          <FontAwesomeIcon icon="pencil-alt" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.edit">Edit</Translate>
                          </span>
                        </Button>
                        <Button tag={Link} to={`${match.url}/${patent.id}/delete`} color="danger" size="sm">
                          <FontAwesomeIcon icon="trash" />{' '}
                          <span className="d-none d-md-inline">
                            <Translate contentKey="entity.action.delete">Delete</Translate>
                          </span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            <div className="alert alert-warning">
              <Translate contentKey="b4KjhralabApp.patent.home.notFound">No Patents found</Translate>
            </div>
          )}
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ patent }: IRootState) => ({
  patentList: patent.entities
});

const mapDispatchToProps = {
  getEntities
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Patent);
